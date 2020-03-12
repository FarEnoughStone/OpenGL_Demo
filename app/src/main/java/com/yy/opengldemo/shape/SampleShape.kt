package com.yy.opengldemo.shape

object SampleShape : Shape() {

    private var positionList1 = floatArrayOf(     // in counterclockwise order:
        0.0f, 0.0f, 0.0f,      // top
        -0.5f, -0.5f, 0.0f,    // bottom left
        0.5f, -0.5f, 0.0f      // bottom right
    )

    private var colorList1 = floatArrayOf(
        1f, 0f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 0f, 1f, 1f
    )

    private var positionList2 = floatArrayOf(     // in counterclockwise order:
        0.0f, 0.5f, 0.0f,      // top
        -0.5f, -0.2f, 0.0f,    // bottom left
        0.5f, -0.2f, 0.0f      // bottom right
    )

    private var colorList2 = floatArrayOf(
        1f, 0f, 0f, 0.5f,
        0f, 1f, 0f, 0.5f,
        0f, 0f, 1f, 0.5f
    )

    private val r = 0.6f
    private var number: Int = 0

    private fun computePosition(angle: Double) {

        positionList2[0] = r * Math.sin(angle).toFloat() + positionList1[0]
        positionList2[1] = r * Math.cos(angle).toFloat() + positionList1[1]

        positionList2[3] = r * Math.sin(angle + Math.PI * 2 / 3).toFloat() + positionList1[0]
        positionList2[4] = r * Math.cos(angle + Math.PI * 2 / 3).toFloat() + positionList1[1]

        positionList2[6] = r * Math.sin(angle + Math.PI * 4 / 3).toFloat() + positionList1[0]
        positionList2[7] = r * Math.cos(angle + Math.PI * 4 / 3).toFloat() + positionList1[1]

    }

    fun move(dx: Float, dy: Float) {
        val temp = floatArrayOf(
            positionList1[0] + dx, positionList1[1] + dy, 0.0f,
            positionList1[3] + dx, positionList1[4] + dy, 0.0f,
            positionList1[6] + dx, positionList1[7] + dy, 0.0f
        )

        positionList1 = temp

    }

    override fun draw(positionHandle: Int?, colorHandle: Int?) {
        number %= 360
        number += 1
        computePosition(Math.PI * number / 180f)

        onDraw(positionHandle, colorHandle, positionList1, colorList1)
        onDraw(positionHandle, colorHandle, positionList2, colorList2)
    }
}