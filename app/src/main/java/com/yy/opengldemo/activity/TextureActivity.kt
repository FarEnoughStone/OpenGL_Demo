package com.yy.opengldemo.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import com.yy.opengldemo.shape.SampleTriangle
import com.yy.opengldemo.texture.SampleTexture
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@SuppressLint("Registered")
class TextureActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mGLView = ColorGLSurfaceView(this)

        setContentView(mGLView)
    }

    class ColorGLSurfaceView(context: Context) : GLSurfaceView(context) {

        init {
            setEGLContextClientVersion(2)
            setRenderer(ColorGLRenderer(context))
        }
    }

    class ColorGLRenderer(val context: Context) : GLSurfaceView.Renderer {

        private var sampleTexture: SampleTexture? = null

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
//            SampleTriangle().draw()
            sampleTexture?.draw()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            val d = Math.min(width, height)
            GLES30.glViewport(0, 0, d, d)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES30.glClearColor(255f, 255f, 255f, 0f)
            sampleTexture = SampleTexture(context)
        }

    }
}