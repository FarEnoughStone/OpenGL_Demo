package com.yy.opengldemo.shape

import com.yy.opengldemo.utils.Utils
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class Sample3D {

    private var positionList = floatArrayOf(
            0f, 0f, 0f,
            -0.5f, 0f, 0.5f,
            0.5f, 0f, 0.5f,
            0f, 0.5f, 0f
    )

    private var flatList = byteArrayOf(
            0, 1, 2,
            0, 1, 3,
            0, 2, 3,
            1, 2, 3
    )

    private var colorList = floatArrayOf(
            1f, 0f, 0f, 0.5f,
            0f, 1f, 0f, 0.5f,
            0f, 0f, 1f, 0.5f,
            0f, 0f, 0f, 0.5f
    )


    private var positionBuffer: FloatBuffer? = null
    private var colorBuffer: FloatBuffer? = null
    private var flatBuffer: ByteBuffer? = null

    init {
        positionBuffer = Utils.getFloatBuffer(positionList)
        colorBuffer = Utils.getFloatBuffer(colorList)
        flatBuffer = Utils.getByteBuffer(flatList)
    }

    private var rotate = 0f

    fun draw(gl: GL10?) {
        // 重置当前的模型视图矩阵
        gl?.glLoadIdentity()
        gl?.glTranslatef(0f, 0f, 0f)
        // 沿着Y轴旋转
        gl?.glRotatef(rotate, 0f, 2f, 0f)
        // 设置顶点的位置数据
        gl?.glVertexPointer(3, GL10.GL_FLOAT, 0, positionBuffer)
        // 设置顶点的颜色数据
        gl?.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer)
        // 按taperFacetsBuffer指定的面绘制三角形
        gl?.glDrawElements(
                GL10.GL_TRIANGLE_STRIP,
                flatBuffer!!.remaining(),
                GL10.GL_UNSIGNED_BYTE,
                flatBuffer
        )
        rotate += 1f

    }
}