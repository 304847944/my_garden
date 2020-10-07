package com.liuchenxi.foundation.texture

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.opengles.GL10
/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
class RenderBuffer(val width: Int, val height: Int, activeTexUnit: Int) {
    var texId = 0

//    private var renderBufferId = 0
    private var frameBufferId = 0

    fun bind() {
        GLES20.glViewport(0, 0, width, height)
        //将纹理图像附加到帧缓冲对象
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId)
        /**
         * target        指定帧缓冲目标。 符号常量必须是GL_FRAMEBUFFER
        attachment        指定应附加纹理图像的附着点。 必须是以下符号常量之一：GL_COLOR_ATTACHMENT0，GL_DEPTH_ATTACHMENT或GL_STENCIL_ATTACHMENT。
        textarget 指定纹理目标。 必须是以下符号常量之一：GL_TEXTURE_2D，GL_TEXTURE_CUBE_MAP_POSITIVE_X，GL_TEXTURE_CUBE_MAP_NEGATIVE_X，GL_TEXTURE_CUBE_MAP_POSITIVE_Y，GL_TEXTURE_CUBE_MAP_NEGATIVE_Y，GL_TEXTURE_CUBE_MAP_POSITIVE_Z或GL_TEXTURE_CUBE_MAP_NEGATIVE_Z。
        texture        指定要附加图像的纹理对象。
        level        指定要附加的纹理图像的mipmap级别，该级别必须为0。
        https://blog.csdn.net/hankern/article/details/88770954
         */
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D, texId, 0
        )
//        //将renderbuffer附加到帧缓冲对象
//        GLES20.glFramebufferRenderbuffer(
//            GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
//            GLES20.GL_RENDERBUFFER, renderBufferId
//        )
    }

    fun unbind() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
//        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0)
    }

    init {
        val genbuf = IntArray(1)
        // Generate and bind 2d texture
        GLES20.glActiveTexture(activeTexUnit)
        texId = MyGLUtils.genTexture()
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            width,
            height,
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            null
        )
        //https://www.it610.com/article/1281538309616582656.htm
//        // Generate frame buffer
        GLES20.glGenFramebuffers(1, genbuf, 0)
        frameBufferId = genbuf[0]
        // Bind frame buffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId)

        // Generate render buffer
//        GLES20.glGenRenderbuffers(1, genbuf, 0)
//        renderBufferId = genbuf[0]
//        // Bind render buffer
//        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBufferId)
        /**
         * glRenderbufferStorage 指定存储在 renderbuffer 中图像的宽高以及颜色格式
         * 并按照此规格为之分配存储空间，当一个渲染缓存被创建，它没有任何数据存储区域
         * 所以我们还要为他分配空间。这可以通过用glRenderbufferStorage()实现
         * 第一个参数必须是GL_RENDERBUFFER。第二个参数可以是用于颜色的（GL_RGB，GL_RGBA，etc.）
         * 用于深度的（GL_DEPTH_COMPONENT），或者是用于模板的格式（GL_STENCIL_INDEX）
         * Width和height是渲染缓存图像的像素维度。
         */
//        GLES20.glRenderbufferStorage(
//            GLES20.GL_RENDERBUFFER,
//            GLES20.GL_DEPTH_COMPONENT16,
//            width,
//            height
//        )
        unbind()
    }
}