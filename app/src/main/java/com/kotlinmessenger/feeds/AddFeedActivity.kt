package com.kotlinmessenger.feeds

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.kotlinmessenger.R
import kotlinx.android.synthetic.main.activity_add_feed.*

class AddFeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_feed)
        button_add_feed.setOnClickListener {
            Toast.makeText(this,"Yet to program",Toast.LENGTH_SHORT).show()
        }


    }
}