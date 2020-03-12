package com.yy.opengldemo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

object Utils {

    //编译着色器程序
    private fun loadShader(type: Int, shaderCode: String): Int {
        //根据着色器种类创建着色器id
        return GLES30.glCreateShader(type).also { shader ->
            //将着色器程序和着色器id进行关联
            GLES30.glShaderSource(shader, shaderCode)
            //将对应着色器进行编译，生成GPU可运行的程序
            GLES30.glCompileShader(shader)
        }
    }

    fun getOpenGLProgram(vertexShaderCode: String, fragmentShaderCode: String): Int {
        //编译着色器
        val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        //创建OpenGL程序id
        return GLES30.glCreateProgram().also {
            //关联顶点着色器
            GLES30.glAttachShader(it, vertexShader)
            //关联片段着色器
            GLES30.glAttachShader(it, fragmentShader)
            //链接
            GLES30.glLinkProgram(it)
        }
    }

    fun getFloatBuffer(array: FloatArray): FloatBuffer {
        return ByteBuffer.allocateDirect(array.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(array)
                position(0)
            }
        }
    }

    fun getByteBuffer(array: ByteArray): ByteBuffer {
        return ByteBuffer.allocateDirect(array.size).apply {
            this.order(ByteOrder.nativeOrder())
            put(array)
            position(0)
        }
    }

    fun getIntBuffer(array: IntArray): IntBuffer {
        return ByteBuffer.allocateDirect(array.size * 4).run {
            order(ByteOrder.nativeOrder())
            asIntBuffer().apply {
                put(array)
                position(0)
            }
        }
    }

    fun getBitmapBuffer(context: Context, fileName: String, result: (ByteBuffer, Bitmap) -> Unit) {
        val inputStream = context.resources.assets.open(fileName)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val b = ByteBuffer.allocate(bitmap.width * bitmap.height * 4)
        bitmap.copyPixelsToBuffer(b)
        b.position(0)
        result(b, bitmap)
    }
}