package com.kotlinmessenger.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.kotlinmessenger.NewMessageActivity
import com.kotlinmessenger.R
import com.kotlinmessenger.RegisterActivity

class LatestMessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        verifyUserIsLoggedIn()
    }

    //If user has already logged in
    private fun verifyUserIsLoggedIn() {
        //get its uid
        val uid=FirebaseAuth.getInstance().uid
        //if uid doesn't exist(User has logged out or hasn't created an account yet)
        if(uid==null){
            //send them to the register page
            val intent= Intent(this, RegisterActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    //handling a menu item click event
   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //when any item on the menu is selected(The item was created in nav_menu)
        when(item?.itemId){
            //if it's new message
            R.id.menu_new_message ->{
                //the the user the the new message page
                val intent=Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }



            //if it's sign out
            R.id.menu_sign_out ->{
                //signout from firebase
                FirebaseAuth.getInstance().signOut()
                //and send the user to the main activity
                val intent=Intent(this, RegisterActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}