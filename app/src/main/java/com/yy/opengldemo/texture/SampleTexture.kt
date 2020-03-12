package com.yy.opengldemo.texture

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES30
import com.yy.opengldemo.utils.Utils
import java.nio.ByteBuffer
import java.nio.FloatBuffer

class SampleTexture(val context: Context) {
    //顶点着色器程序 用于GPU运行
    private val vertexShaderCode =
            "precision mediump float;" +
                    "attribute vec4 a_Position;" +
                    "attribute vec2 a_textureCoordinate;" +
                    "varying vec2 v_textureCoordinate;" +
                    "void main() {" +
                    "   v_textureCoordinate = a_textureCoordinate;" +
                    "   gl_Position = a_Position;" +
                    "}"
    //片段着色器程序 用于GPU运行
    private val fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec2 v_textureCoordinate;" +
                    "uniform sampler2D u_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(u_texture, v_textureCoordinate);" +
                    "}"

    private var positionList = floatArrayOf(
            -0.5f, 1f, 0f,
            -0.5f, 0f, 0f,
            0.5f, 1f, 0f,
            0.5f, 0f, 0f
    )

    private var texturePositionList = floatArrayOf(
            0f, 1f,
            0f, 0f,
            1f, 1f,
            1f, 0f
    )

    private var program = 0
    private var positionBuffer: FloatBuffer? = null
    private var texturePositionBuffer: FloatBuffer? = null
    private var positionHandle: Int? = null
    private var texturePositionHandle: Int? = null
    private var textureHandle: Int? = null

    init {
        program = Utils.getOpenGLProgram(vertexShaderCode, fragmentShaderCode)
        GLES30.glUseProgram(program)
        positionBuffer = Utils.getFloatBuffer(positionList)
        texturePositionBuffer = Utils.getFloatBuffer(texturePositionList)
        positionHandle = GLES30.glGetAttribLocation(program, "a_Position")
        texturePositionHandle = GLES30.glGetAttribLocation(program, "a_textureCoordinate")
        textureHandle = GLES30.glGetAttribLocation(program, "u_texture")

//        val textures = IntArray(1)
//        GLES30.glGenTextures(textures.size, textures, 0)
//        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0])

//        Utils.getBitmapBuffer(context,"texture/地板.jpg"){ byteBuffer, bitmap ->
//            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
//            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D,
//                0,
//                GLES30.GL_RGBA,
//                bitmap.width,
//                bitmap.height,
//                0,
//                GLES30.GL_RGBA,
//                GLES30.GL_UNSIGNED_BYTE,
//                byteBuffer)
//        }

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        Utils.getBitmapBuffer(context,"texture/凉席.jpg"){ byteBuffer, bitmap ->
            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D,
                0,
                GLES30.GL_RGBA,
                bitmap.width,
                bitmap.height,
                0,
                GLES30.GL_RGBA,
                GLES30.GL_UNSIGNED_BYTE,
                byteBuffer)
        }

        GLES30.glEnableVertexAttribArray(positionHandle!!)
        GLES30.glEnableVertexAttribArray(texturePositionHandle!!)
        GLES30.glEnableVertexAttribArray(textureHandle!!)

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)

    }

    fun draw() {
        positionHandle?.let {
            // 加载顶点数据
            GLES30.glVertexAttribPointer(
                    it,
                    3,
                    GLES30.GL_FLOAT,
                    false,
                    3 * 4,
                    positionBuffer
            )
        }

        texturePositionHandle?.let {
            GLES30.glVertexAttribPointer(
                    it,
                    2,
                    GLES30.GL_FLOAT,
                    false,
                    2 * 4,
                    texturePositionBuffer
            )
        }

        textureHandle?.let {
            GLES30.glUniform1i(it, 0)
        }

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, positionList.size / 3)
    }
}