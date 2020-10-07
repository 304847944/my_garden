package com.liuchenxi.foundation.texture

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.opengl.EGL14.*
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import android.util.Pair
import android.view.TextureView
import java.io.IOException
import javax.microedition.khronos.egl.*

public class CameraRenderer : Runnable, TextureView.SurfaceTextureListener {
    val TAG = "CameraRenderer"
    companion object {
        val DRAW_INTERVAL = 1000 / 30

        val mCameraRenderer: CameraRenderer by lazy {
            CameraRenderer()
        }
    }
    private var renderThread: Thread? = null
    lateinit var context: Context
    private var surfaceTexture: SurfaceTexture? = null
    private var gwidth: Int = 0
    private var gheight: Int = 0

    private var eglDisplay: EGLDisplay? = null
    private var eglSurface: EGLSurface? = null
    private var eglContext: EGLContext? = null
    private lateinit var egl10: EGL10

    private var camera: Camera? = null
    private var cameraSurfaceTexture: SurfaceTexture? = null
    private var cameraTextureId: Int = 0

    private var selectedFilter: CameraFilter? = null

    public fun CameraRenderInit(context: Context){
        this.context = context
    }

    override fun run() {
        initGL(surfaceTexture!!)
        selectedFilter =  CameraFilter(context)
//        if (selectedFilter != null) selectedFilter!!.onAttach()
        // Create texture for camera preview
        cameraTextureId = MyGLUtils.genTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
        //https://upload-images.jianshu.io/upload_images/6126848-99a924ba8ae950f8.png
        cameraSurfaceTexture = SurfaceTexture(cameraTextureId)
        // Start camera preview
        try {
            //SurfaceTexture的updateTexImage方法会更新接收到的预览数据到
            // 其绑定的OpenGL纹理中。该纹理会默认绑定到OpenGL
            // Context的GL_TEXTURE_EXTERNAL_OES纹理目标对象中。
            // GL_TEXTURE_EXTERNAL_OES是OpenGL中一个特殊的纹理目标对象，
            // 与GL_TEXTURE_2D是同级的
            camera!!.setPreviewTexture(cameraSurfaceTexture)
            camera!!.startPreview()
        } catch (ioe: IOException) {
            // Something bad happened
        }
        // Render loop
        while (!Thread.currentThread().isInterrupted) {
            try {
                if (gwidth < 0 && gheight < 0) {
                    gwidth = -gwidth
                    gheight = -gheight
                        GLES20.glViewport(
                        0,
                        0,
                        gwidth ,gheight
                    )
                }
//                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

                // Update the camera preview textureFz
                synchronized(this) { cameraSurfaceTexture!!.updateTexImage() }

                // Draw camera preview
                selectedFilter!!.draw(cameraTextureId, gwidth, gheight)

                // Flush
                GLES20.glFlush()
                egl10.eglSwapBuffers(eglDisplay, eglSurface)
                Thread.sleep(CameraRenderer.DRAW_INTERVAL.toLong())
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }

        cameraSurfaceTexture!!.release()
        GLES20.glDeleteTextures(1, intArrayOf(cameraTextureId), 0)
    }


    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, width: Int, height: Int) {
//        gwidth = -width
//        gheight = -height
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
        if (camera != null) {
            camera!!.release()
            camera!!.stopPreview()
        }
        if (renderThread != null && renderThread!!.isAlive) {
            renderThread!!.interrupt()
        }
        CameraFilter.release()

        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        if (renderThread != null && renderThread!!.isAlive) {
            renderThread!!.interrupt()
        }
        renderThread = Thread(this)

        surfaceTexture = surface
        gwidth = width
        gheight = height
        // Open camera
        val backCamera: Pair<CameraInfo, Int>? = getBackCamera()
        val backCameraId = backCamera!!.second
        camera = Camera.open(backCameraId)
        // Start rendering
        renderThread!!.start()
    }

    public fun initGL(texture: SurfaceTexture) {
        //http://www.khronos.org/registry/EGL/sdk/docs/man/html/eglGetDisplay.xhtml
        egl10 = EGLContext.getEGL() as EGL10
        eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        if (eglDisplay === EGL10.EGL_NO_DISPLAY) {
            com.orhanobut.logger.Logger.e(
                "eglGetDisplay failed " + android.opengl.GLUtils.getEGLErrorString(
                    egl10.eglGetError()
                )
            )
        }

        val version = IntArray(2)//主次版本号
        //https://www.zybuluo.com/cxm-2016/note/572030#eglinitialize
        if (!egl10.eglInitialize(eglDisplay, version)) {
            throw RuntimeException(
                "eglInitialize failed " + android.opengl.GLUtils.getEGLErrorString(
                    egl10.eglGetError()
                )
            )
        }

        val configsCount = IntArray(1)
        val configs = arrayOfNulls<EGLConfig>(1)
        val configSpec = intArrayOf(
            EGL10.EGL_RENDERABLE_TYPE,
            EGL_OPENGL_ES2_BIT,
            EGL10.EGL_RED_SIZE,
            8,
            EGL10.EGL_GREEN_SIZE,
            8,
            EGL10.EGL_BLUE_SIZE,
            8,
            EGL10.EGL_ALPHA_SIZE,
            8,
            EGL10.EGL_DEPTH_SIZE,
            0,
            EGL10.EGL_STENCIL_SIZE,
            0,
            EGL10.EGL_NONE
        )

        var eglConfig: EGLConfig? = null

        require(
            /**
            eglDisplay：已连接的设备
            configSpec：传入的配置信息数组
            configs：保存返回的配置信息的数组
            config_size：传入的配置数组的长度
            configsCount：保存返回的配置信息的数组的长度
             */
            egl10.eglChooseConfig(
                eglDisplay,
                configSpec,
                configs,
                1,
                configsCount
            )
        ) { "eglChooseConfig failed " + GLUtils.getEGLErrorString(egl10.eglGetError()) }
        if (configsCount[0] > 0) {
            eglConfig = configs[0]
        }
        if (eglConfig == null) {
            throw RuntimeException("eglConfig not initialized")
        }

        val attrib_list = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
        //渲染上下文是OpenGL ES的内部数据结构，包含操作所需的所有状态信息。例如程序中使用的顶点着色器或者片元着色器的引用
        //关于顶点着色器和片源着色器稍后再说
        /**
         * display：指定显示连接
        config：指定配置对象
        shareContext：允许多个EGL上下文共享特定的数据，EGL_NO_CONTEXT参数表示没有共享
        attribList：指定创建上下文使用的属性列表
        return：创建的上下文对象
         */
        eglContext =
            egl10.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list)

        eglSurface = egl10.eglCreateWindowSurface(eglDisplay, eglConfig, texture, null)

        if (eglSurface == null || eglSurface === EGL10.EGL_NO_SURFACE) {
            val error = egl10.eglGetError()
            if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
                Log.e(TAG, "eglCreateWindowSurface returned EGL10.EGL_BAD_NATIVE_WINDOW")
                return
            }
            throw RuntimeException(
                "eglCreateWindowSurface failed " + android.opengl.GLUtils.getEGLErrorString(
                    error
                )
            )
        }

        if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            throw RuntimeException(
                "eglMakeCurrent failed " + android.opengl.GLUtils.getEGLErrorString(
                    egl10.eglGetError()
                )
            )
        }
    }

    private fun getBackCamera(): Pair<CameraInfo, Int>? {
        val cameraInfo = CameraInfo()
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            Camera.getCameraInfo(i, cameraInfo)
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                return Pair(cameraInfo, i)
            }
        }
        return null
    }

    fun distory(){
        egl10.eglMakeCurrent(eglDisplay, null,null,null)
        eglDisplay = null
        eglSurface = null
        eglContext = null

    }
}