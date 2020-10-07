package com.liuchenxi.foundation.texture

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import androidx.annotation.CallSuper
import com.liuchenxi.foundation.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
class CameraFilter(context: Context?) {
    //定点着色器交互
    fun draw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        // Use shaders // 使用shader程序
        GLES20.glUseProgram(PROGRAM)
        val iChannel0Location =
            GLES20.glGetUniformLocation(PROGRAM, "iChannel0")

        GLES20.glUniform1i(iChannel0Location, 0)
        val vPositionLocation =
            GLES20.glGetAttribLocation(PROGRAM, "vPosition")
        GLES20.glEnableVertexAttribArray(vPositionLocation)
        GLES20.glVertexAttribPointer(
            vPositionLocation,
            2,
            GLES20.GL_FLOAT,
            false,
            4 * 2,
            VERTEX_BUF
        )
        val vTexCoordLocation =
            GLES20.glGetAttribLocation(PROGRAM, "vTexCoord")
        GLES20.glEnableVertexAttribArray(vTexCoordLocation)
        GLES20.glVertexAttribPointer(
            vTexCoordLocation,
            2,
            GLES20.GL_FLOAT,
            false,
            4 * 2,
            ROATED_TEXTURE_COORD_BUF
        )
        // Create camera render buffer
        if (CAMERA_RENDER_BUF == null || CAMERA_RENDER_BUF!!.width !== canvasWidth || CAMERA_RENDER_BUF!!.height !== canvasHeight
        ) {
            CAMERA_RENDER_BUF =
                RenderBuffer(canvasWidth, canvasHeight, cameraTexId)
        }
        // Render to texture
        CAMERA_RENDER_BUF!!.bind()
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        CAMERA_RENDER_BUF!!.unbind()

//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
//        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTexId)
        onDraw(CAMERA_RENDER_BUF!!.texId, canvasWidth, canvasHeight)
    }

    fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        setupShaderInputs(
            program,
            TEXTURE_COORD_BUF,
            cameraTexId
        )
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    fun setupShaderInputs(
        program: Int,
        textureCoord: FloatBuffer?,
        iChannels: Int
    ) {
        GLES20.glUseProgram(program)
        val vTexCoordLocation = GLES20.glGetAttribLocation(program, "vTexCoord")
        //https://blog.csdn.net/flycatdeng/article/details/82667052
        GLES20.glEnableVertexAttribArray(vTexCoordLocation)
        //https://blog.csdn.net/flycatdeng/article/details/82667374
        GLES20.glVertexAttribPointer(
            vTexCoordLocation,
            2,
            GLES20.GL_FLOAT,
            false,
            4 * 2,
            textureCoord
        )
        val sTextureLocation = GLES20.glGetUniformLocation(program, "iChannel0")
        //必须要有纹理单元操作的，理由是：https://blog.csdn.net/huazi5dgan/article/details/76160334
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iChannels)

        GLES20.glUniform1i(sTextureLocation, 0)
    }


    companion object {
        val SQUARE_COORDS = floatArrayOf(
            1.0f, -1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            -1.0f, 1.0f
        )
        val TEXTURE_COORDS = floatArrayOf(
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f
        )
        var VERTEX_BUF: FloatBuffer? = null
        var TEXTURE_COORD_BUF: FloatBuffer? = null
        var PROGRAM = 0
        private const val BUF_ACTIVE_TEX_UNIT = GLES20.GL_TEXTURE8
        private var CAMERA_RENDER_BUF: RenderBuffer? = null
        private val ROATED_TEXTURE_COORDS = floatArrayOf(
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
        )
        private var ROATED_TEXTURE_COORD_BUF: FloatBuffer? = null
        fun release() {
            PROGRAM = 0
            CAMERA_RENDER_BUF = null
        }
    }

    val program: Int

    init {
        // Setup default Buffers
        if (VERTEX_BUF == null) {
            /**
            ByteBuffer vbb = ByteBuffer.allocateDirect(arrs.length * 4);
            vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
            fbResult = vbb.asFloatBuffer();// 转换为Float型缓冲
            fbResult.put(arrs);// 向缓冲区中放入顶点坐标数据
            fbResult.position(0);// 设置缓冲区起始位置
             */
            VERTEX_BUF =
                ByteBuffer.allocateDirect(SQUARE_COORDS.size * 4)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
            VERTEX_BUF?.put(SQUARE_COORDS)
            VERTEX_BUF?.position(0)
        }
        if (TEXTURE_COORD_BUF == null) {
            //https://blog.csdn.net/seebetpro/article/details/49184305
            /**
            ByteBuffer vbb = ByteBuffer.allocateDirect(arrs.length * 4);
            vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
            fbResult = vbb.asFloatBuffer();// 转换为Float型缓冲
            fbResult.put(arrs);// 向缓冲区中放入顶点坐标数据
            fbResult.position(0);// 设置缓冲区起始位置
             */
            TEXTURE_COORD_BUF =
                ByteBuffer.allocateDirect(TEXTURE_COORDS.size * 4)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
            TEXTURE_COORD_BUF?.put(TEXTURE_COORDS)
            TEXTURE_COORD_BUF?.position(0)
        }
        /**
        ByteBuffer vbb = ByteBuffer.allocateDirect(arrs.length * 4);
        vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
        fbResult = vbb.asFloatBuffer();// 转换为Float型缓冲
        fbResult.put(arrs);// 向缓冲区中放入顶点坐标数据
        fbResult.position(0);// 设置缓冲区起始位置
         */
        if (ROATED_TEXTURE_COORD_BUF == null) {
            ROATED_TEXTURE_COORD_BUF =
                ByteBuffer.allocateDirect(ROATED_TEXTURE_COORDS.size * 4)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
            ROATED_TEXTURE_COORD_BUF?.put(ROATED_TEXTURE_COORDS)
            ROATED_TEXTURE_COORD_BUF?.position(0)
        }

        if (PROGRAM == 0) {
            PROGRAM =
                MyGLUtils.buildProgram(context!!, R.raw.vertext, R.raw.original_rtt)
        }
        program = MyGLUtils.buildProgram(context!!, R.raw.vertext, R.raw.water_reflection)
    }
}