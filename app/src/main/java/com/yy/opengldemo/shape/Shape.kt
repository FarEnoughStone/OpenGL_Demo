package com.yy.opengldemo.shape

import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

abstract class Shape {
    val POSITION_PER_VERTEX = 3 //点的属性数 x y z
    val COLOR_PER_VERTEX = 4 //颜色的属性数 r g b a

    open fun draw(positionHandle: Int?, colorHandle: Int?) {}

    fun getFloatBuffer(positionArray: FloatArray): FloatBuffer {
        return ByteBuffer.allocateDirect(positionArray.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(positionArray)
                position(0)
            }
        }
    }

    protected fun onDraw(positionHandle: Int?, colorHandle: Int?, positionList: FloatArray,
                         colorList: FloatArray, drawMode: Int = GLES30.GL_TRIANGLES) {
        if (positionHandle == null || colorHandle == null) {
            return
        }

        // 加载顶点数据
        GLES30.glVertexAttribPointer(
                positionHandle,
                SampleShape.POSITION_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                SampleShape.POSITION_PER_VERTEX * 4,
                getFloatBuffer(positionList)
        )
        // 加载颜色数据
        GLES30.glVertexAttribPointer(
                colorHandle,
                SampleShape.COLOR_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                SampleShape.COLOR_PER_VERTEX * 4,
                getFloatBuffer(colorList)
        )
        // 绘制三角形
        GLES30.glDrawArrays(drawMode, 0, positionList.size / POSITION_PER_VERTEX)
    }

}