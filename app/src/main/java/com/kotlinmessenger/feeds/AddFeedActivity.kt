package com.kotlinmessenger.feeds

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.kotlinmessenger.R
import kotlinx.android.synthetic.main.activity_add_feed.*
import org.w3c.dom.Text

class AddFeedActivity : AppCompatActivity() {
    lateinit var inputText:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_feed)
        button_add_feed.setOnClickListener {
            Toast.makeText(this,"Yet to program",Toast.LENGTH_SHORT).show()
        }

    editText_add_feed.addTextChangedListener(object:TextWatcher
    {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(editText_add_feed.text.toString().length < 250){
                textView_charCount.text = (250 - editText_add_feed.text.toString().length).toString()
                textView_charCount.setTextColor(Color.BLACK)
            }
            else{
                textView_charCount.text = "0"
                textView_charCount.setTextColor(Color.RED)
            }


        }
        override fun afterTextChanged(s: Editable?) {

        }

    })
    }
}