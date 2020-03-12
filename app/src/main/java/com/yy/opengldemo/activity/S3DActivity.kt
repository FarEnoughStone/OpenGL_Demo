package com.yy.opengldemo.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Bundle
import android.view.MotionEvent
import com.yy.opengldemo.shape.Sample3D
import com.yy.opengldemo.utils.Utils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@SuppressLint("Registered")
class S3DActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mGLView = ColorGLSurfaceView(this)
        setContentView(mGLView)
    }

    class ColorGLSurfaceView(context: Context) : GLSurfaceView(context) {

        private var renderer: ColorGLRenderer? = null

        init {
            /**
             * 使用gl千万不要设置这个
             * 设置版本3 就要使用GLES30
             */
//            setEGLContextClientVersion(3)
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
//                    requestRender()
                }
            }

            return true
        }
    }

    class ColorGLRenderer : GLSurfaceView.Renderer {

        private var sample3D: Sample3D? = null

        var mMVPMatrix = FloatArray(16)
        var mProjectionMatrix = FloatArray(16)
        var mViewMatrix = FloatArray(16)

        override fun onDrawFrame(gl: GL10?) {

            Matrix.setLookAtM(
                mViewMatrix, 0, 0f, 1f, -3f,
                0f, 0.5f, 0f, 0f, 1.0f, 0.0f
            )
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)
            gl?.glLoadMatrixf(Utils.getFloatBuffer(mMVPMatrix))

            gl?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)

            sample3D?.draw(gl)

            gl?.glFinish()

        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            val d = Math.min(width, height)
            gl?.glViewport(0, 0, d, d)

            // 将当前矩阵模式设为投影矩阵
            gl?.glMatrixMode(GL10.GL_PROJECTION)
            // 初始化单位矩阵
            gl?.glLoadIdentity()
            // 调用此方法设置透视视窗的空间大小。
            gl?.glFrustumf(-1f, 1f, -1f, 1f, -5f, 5f)

            Matrix.frustumM(mProjectionMatrix, 0, -1f, 1f, -1f, 1f, 3f, 5f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            gl?.glClearColor(255f, 255f, 255f, 0f)
            sample3D = Sample3D()

            gl?.glEnable(GLES30.GL_BLEND)
            gl?.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)

            // 关闭抗抖动
            gl?.glDisable(GL10.GL_DITHER)
            // 设置系统对透视进行修正
            gl?.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)

            // 设置阴影平滑模式
            gl?.glShadeModel(GL10.GL_SMOOTH)
            // 启用深度测试
            gl?.glEnable(GL10.GL_DEPTH_TEST)
            // 设置深度测试的类型
            gl?.glDepthFunc(GL10.GL_LEQUAL)

            // 启用顶点座标数据
            gl?.glEnableClientState(GL10.GL_VERTEX_ARRAY)
            // 启用顶点颜色数据
            gl?.glEnableClientState(GL10.GL_COLOR_ARRAY)
            // 设置当前矩阵模式为模型视图
            gl?.glMatrixMode(GL10.GL_MODELVIEW)

        }

    }
}