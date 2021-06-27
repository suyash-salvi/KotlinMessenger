package com.kotlinmessenger.models

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotlinmessenger.NewMessageActivity
import com.kotlinmessenger.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        splashScreen_Image.alpha = 0f

        splashScreen_Image.animate().setDuration(1500).alpha(1f).withEndAction {
            val intent = Intent(this, NewMessageActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}