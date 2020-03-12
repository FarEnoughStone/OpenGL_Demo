package com.yy.opengldemo.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Bundle
import android.view.MotionEvent
import com.yy.opengldemo.shape.SampleShape
import com.yy.opengldemo.shape.TriangleStereoscopic
import com.yy.opengldemo.utils.ShaderUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@SuppressLint("Registered")
class ColorActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mGLView = ColorGLSurfaceView(this)

        setContentView(mGLView)
    }

    class ColorGLSurfaceView(context: Context) : GLSurfaceView(context) {

        private var renderer: ColorGLRenderer? = null

        init {
            setEGLContextClientVersion(3)
            renderer = ColorGLRenderer()
            setRenderer(renderer)
//            renderMode = RENDERMODE_WHEN_DIRTY
        }

        var oldX = 0f
        var oldY = 0f
        override fun onTouchEvent(event: MotionEvent?): Boolean {

            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    oldX = event.x
                    oldY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    SampleShape.move((event.x - oldX) / 540f, (oldY - event.y) / 540f)
                    oldX = event.x
                    oldY = event.y
//                    requestRender()
                }
            }

            return true
        }
    }

    class ColorGLRenderer : GLSurfaceView.Renderer {

        var positionHandle: Int? = null
        var colorHandle: Int? = null

        var mMVPMatrix = FloatArray(16)
        var mProjectionMatrix = FloatArray(16)
        var mViewMatrix = FloatArray(16)


        override fun onDrawFrame(gl: GL10?) {
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

            Matrix.setLookAtM(
                mViewMatrix, 0, 0f, 0f, -3f,
                0f, 0f, 0f, 0f, 1.0f, 0.0f
            )
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

            SampleShape.draw(positionHandle, colorHandle)
//            TriangleStereoscopic.draw(positionHandle, colorHandle)

        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            val d = Math.min(width, height)
            GLES30.glViewport(0, 0, d, d)

            Matrix.frustumM(mProjectionMatrix, 0, -1f, 1f, -1f, 1f, -5f, 5f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES30.glClearColor(255f, 255f, 255f, 0f)
            ShaderUtil.initOpenGLProgram()
            positionHandle = ShaderUtil.getPositionHandle()
            colorHandle = ShaderUtil.getColorHandle()

            GLES30.glEnableVertexAttribArray(positionHandle!!)
            GLES30.glEnableVertexAttribArray(colorHandle!!)

            GLES30.glEnable(GLES30.GL_BLEND)
            GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)
        }

    }
}