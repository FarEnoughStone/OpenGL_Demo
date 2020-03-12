package com.yy.opengldemo.utils

import android.opengl.GLES30

object ShaderUtil {
    //顶点着色器程序 用于GPU运行
    private val vertexShaderCode =
            "precision mediump float;" +
                    "attribute vec4 a_Position;" +
                    "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 a_Color;" +
                    "varying vec4 v_Color;" +
                    "void main() {" +
                    "   v_Color = a_Color;" +
                    "   gl_Position = uMVPMatrix*a_Position;" +
                    "}"
    //片段着色器程序 用于GPU运行
    private val fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "void main() {" +
                    "  gl_FragColor = v_Color;" +
                    "}"

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

    var mProgram = 0

    fun initOpenGLProgram() {
        //编译着色器
        val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        //创建OpenGL程序id
        mProgram = GLES30.glCreateProgram().also {
            //关联顶点着色器
            GLES30.glAttachShader(it, vertexShader)
            //关联片段着色器
            GLES30.glAttachShader(it, fragmentShader)
            //链接
            GLES30.glLinkProgram(it)
        }

        // 让OpenGL ES使用 编译链接好的程序
        GLES30.glUseProgram(mProgram)
    }

    fun getPositionHandle(): Int {
        return GLES30.glGetAttribLocation(mProgram, "a_Position")
    }

    fun getColorHandle(): Int {
//        return GLES30.glGetUniformLocation(mProgram, "v_Color")
        return GLES30.glGetAttribLocation(mProgram, "a_Color")
    }
}