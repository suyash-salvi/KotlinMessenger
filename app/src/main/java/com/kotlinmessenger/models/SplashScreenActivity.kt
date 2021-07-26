package com.kotlinmessenger.models

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotlinmessenger.NewMessageActivity
import com.kotlinmessenger.R
import com.kotlinmessenger.RegisterActivity
import com.kotlinmessenger.messages.LatestMessagesActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.lang.System.load

class SplashScreenActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /*Picasso.get().load(R.raw.splashimage).into(splashScreen_Image)*/
        splashScreen_Image.alpha = 0f


        splashScreen_Image.animate().setDuration(1500).alpha(1f).withEndAction {
            val intent = Intent(this, LatestMessagesActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}