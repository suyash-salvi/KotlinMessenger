package com.kotlinmessenger.feeds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kotlinmessenger.NewMessageActivity
import com.kotlinmessenger.R
import com.kotlinmessenger.RegisterActivity
import com.kotlinmessenger.messages.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.activity_latest_messages.*

class FeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        feed_floatingActionButton.setOnClickListener {

            val intent=Intent(this,AddFeedActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.feedmenu_liked_posts->{
                Toast.makeText(this,"Liked posts still not available",Toast.LENGTH_LONG).show()
            }
            R.id.feed_to_chatlog->{
                //the the user the the new message page
                val intent=Intent(this, LatestMessagesActivity::class.java)
                startActivity(intent)

            }
            R.id.feedmenu_sign_out->{
                //signout from firebase
                FirebaseAuth.getInstance().signOut()
                //and send the user to the main activity
                val intent= Intent(this, RegisterActivity::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    //For using custom menubar
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feed_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }
}