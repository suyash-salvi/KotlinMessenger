package com.kotlinmessenger.registerLogin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kotlinmessenger.R
import com.kotlinmessenger.messages.ChatLogActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //
        back_to_register.setOnClickListener {
            Log.d("LoginActivity","Get back to the register page")
           finish()
        }
        btn_login.setOnClickListener {
            val email=login_email.text.toString()
            val password=login_password.text.toString()
            Log.d("LoginActivity","New Registered Email is $email")
            Log.d("LoginActivity","The password for the above email is $password")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnFailureListener{
                        Toast.makeText(this,"There's something wrong with the email or the password",Toast.LENGTH_SHORT).show()
                    }
            if(email.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"Please enter the required details",Toast.LENGTH_SHORT).show()
            }




        }

    }
}