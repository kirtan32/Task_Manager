package com.kirtan.shah.taskmanager.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.kirtan.shah.taskmanager.MainActivity
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.onboardintro.introActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val timeout = 3000
        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, introActivity::class.java)
            startActivity(intent)
            finish()
        }, timeout.toLong())

    }
}