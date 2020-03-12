package com.yy.opengldemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yy.opengldemo.activity.ColorActivity
import com.yy.opengldemo.activity.S3DActivity
import com.yy.opengldemo.activity.TextureActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        to_color_sv.setOnClickListener {
            val intent = Intent(this, ColorActivity::class.java)
//            val intent = Intent(this, TextureActivity::class.java)
            startActivity(intent)
        }

        to_3d_sv.setOnClickListener {
            val intent = Intent(this, S3DActivity::class.java)
            startActivity(intent)
        }
    }
}
