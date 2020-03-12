package com.yy.opengldemo.shape

import android.opengl.GLES30

object TriangleStereoscopic : Shape() {
//    private var positions = floatArrayOf(
//            0f, 0f, 0f,
//            -0.5f, 0f, 0.5f,
//            0.5f, 0f, 0.5f,
//            0f, 0.5f, 0.3f
//    )

    private var positionList1 = floatArrayOf(
            -0.5f, 1f, 0f,
            -0.5f, 0f, 0f,
            0.5f, 1f, 0f,
            0.5f, 0f, 0f
    )

    private var colorList = floatArrayOf(
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            0f, 0f, 0f, 1f
    )

    override fun draw(positionHandle: Int?, colorHandle: Int?) {
        onDraw(positionHandle, colorHandle, positionList1, colorList, GLES30.GL_TRIANGLE_STRIP)

    }
}