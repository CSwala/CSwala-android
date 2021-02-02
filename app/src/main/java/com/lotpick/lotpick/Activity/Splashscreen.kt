package com.lotpick.lotpick.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lotpick.lotpick.R

class Splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val tv = findViewById<TextView>(R.id.tv)
        val tv1 = findViewById<TextView>(R.id.textView2)
        val splash = 2500
        val animation = AnimationUtils.loadAnimation(this@Splashscreen, R.anim.text_anim)
        tv.startAnimation(animation)
        tv1.startAnimation(animation)
        Handler().postDelayed({
            startActivity(Intent(this@Splashscreen, Login::class.java))
            finish()
        }, splash.toLong())
    }
}
