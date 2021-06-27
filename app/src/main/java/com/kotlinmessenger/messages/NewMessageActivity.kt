package com.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.multidex.MultiDex
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.kotlinmessenger.messages.ChatLogActivity
import com.kotlinmessenger.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import okhttp3.EventListener

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        recyclerview_newmessage.layoutManager=LinearLayoutManager(this@NewMessageActivity,LinearLayoutManager.VERTICAL,false)


        fetchUsers()





    }

    companion object{
        val USER_KEY="USER_KEY"
    }


    private fun fetchUsers() {
        //get reference of users
        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @Override
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)

                    if (user != null) {
                        //Add users to the list
                        adapter.add(UserItem(user))
                    }
                    adapter.setOnItemClickListener { item, view ->
                        //casting user item into an object
                        val userItem= item as UserItem

                        //open a new activity with a chatlog of the selected user
                        val intent = Intent(view.context, ChatLogActivity::class.java)

                        //used for getting the user outside of an item
                        //intent.putExtra(USER_KEY,userItem.user.username)
                        intent.putExtra(USER_KEY,userItem.user)




                        //startactivity
                       startActivity(intent)

                        //finish the newmessage activity so that pressing back brings you chat
/*
                        finish()
*/
                    }
                    recyclerview_newmessage.adapter = adapter

                }
            }


            override fun onCancelled(error: DatabaseError) {
                Log.d("NewMessage", "You have reached to snapshot")

            }
        })

    }

}

    class UserItem(val user:User):Item<GroupieViewHolder>(){
        //we can access particular UI component
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            //will be called in our list for each user item later on...
            viewHolder.itemView.username_textview_new_message.text=user.username
            //
            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView)

        }

        override fun getLayout(): Int {
        return R.layout.user_row_new_message
        }
    }

