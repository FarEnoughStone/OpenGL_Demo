package com.yy.opengldemo.shape

import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class SampleTriangle {
    //顶点数量
    val COORDS_PER_VERTEX = 3
    val triangleCoords = floatArrayOf(     // in counterclockwise order:
        0.0f, 0.622008459f, 0.0f,      // top
        -0.5f, -0.311004243f, 0.0f,    // bottom left
        0.5f, -0.311004243f, 0.0f      // bottom right
    )

    //世界坐标
    val worldPosition = floatArrayOf(
        0.2f, 0.2f, 0f, 1f
    )
    //将顶点放入buffer
    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(triangleCoords)
                position(0)
            }
        }

    var mProgram: Int = 0

    //顶点着色器程序 用于GPU运行
    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

    //顶点着色器程序 用于GPU运行
    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
        "uniform vec4 world_Position;" +
                "void main() {" +
                "  gl_Position = vec4(" +
                "       vPosition.x * world_Position.w + world_Position.x," +
                "       vPosition.y * world_Position.w + world_Position.y," +
                "       vPosition.z * world_Position.w + world_Position.z," +
                "       vPosition.w)" +
                "}"
    //片段着色器程序 用于GPU运行
    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    //编译着色器程序
    fun loadShader(type: Int, shaderCode: String): Int {
        //根据着色器种类创建着色器id
        return GLES30.glCreateShader(type).also { shader ->
            //将着色器程序和着色器id进行关联
            GLES30.glShaderSource(shader, shaderCode)
            //将对应着色器进行编译，生成GPU可运行的程序
            GLES30.glCompileShader(shader)
        }
    }

    init {
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
    }

    /**
     * 以上代码属于手动编译OpenGL程序 —— 编译、链接
     */


    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    fun draw() {
        // 让OpenGL ES使用 编译链接好的程序
        GLES30.glUseProgram(mProgram)

        // 获取 mProgram 程序中 类型为attribute 的 "vPosition" 变量 （的指针）
        // 注意：变量名须于自己编写的程序对应
        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition").also {

            // 使能顶点数据 使GPU能够读取到顶点数据
            GLES30.glEnableVertexAttribArray(it)

            // 加载顶点数据
            GLES30.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                COORDS_PER_VERTEX * 4,
                vertexBuffer
            )

//            函数原型：
//            void glVertexAttribPointer (int index, int size, int type, boolean normalized, int stride, Buffer ptr )
//            参数含义：
//            index  指定要修改的顶点着色器中顶点变量id；
//            size   指定每个顶点属性的组件数量。必须为1、2、3或者4。如position是由3个（x,y,z）组成，而颜色是4个（r,g,b,a））；
//            type   指定数组中每个组件的数据类型。可用的符号常量有GL_BYTE, GL_UNSIGNED_BYTE, GL_SHORT,GL_UNSIGNED_SHORT, GL_FIXED, 和 GL_FLOAT，初始值为GL_FLOAT；
//            normalized  指定当被访问时，固定点数据值是否应该被归一化（GL_TRUE）或者直接转换为固定点值（GL_FALSE）；
//            stride      指定连续顶点属性之间的偏移量。如果为0，那么顶点属性会被理解为：它们是紧密排列在一起的。初始值为0。如果normalized被设置为GL_TRUE，意味着整数型的值会被映射至区间[-1,1](有符号整数)，或者区间[0,1]（无符号整数），反之，这些值会被直接转换为浮点值而不进行归一化处理；
//            ptr  顶点的缓冲数据。

            // 获取 mProgram 程序中 类型为uniform 的 "vColor" 变量 （的指针）
            // 注意：变量名须于自己编写的程序对应
            mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // 加载颜色数据
                GLES30.glUniform4fv(colorHandle, 1, color, 0)

//                函数原型：
//                void glUniform4fv(GLint location,  GLsizei count, const GLfloat *value );
//                location：变量指针
//                count：要修改的数量
//                value：数组
//                offset：数组偏移
//                glUniform 修改类型为Uniform的变量；4 4个一组的属性；f 浮点型；v 指针型
            }

            // 绘制三角形
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

            // 去使能顶点数据
            GLES30.glDisableVertexAttribArray(it)
        }
    }
}