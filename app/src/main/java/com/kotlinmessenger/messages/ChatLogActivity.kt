package com.kotlinmessenger.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.kotlinmessenger.NewMessageActivity
import com.kotlinmessenger.R
import com.kotlinmessenger.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

//chatlog activity
class ChatLogActivity : AppCompatActivity() {

    companion object{
        val TAG="Chatlog"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //layout xml
        setContentView(R.layout.activity_chat_log)
        //actionbar title


        //getting the USER_KEY from New Message Activity
        //val username=intent.getStringExtra(NewMessageActivity.USER_KEY)
        val user=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title=user?.username


        stupDummyData()

        send_button_chat_log.setOnClickListener {
            Log.d(TAG,"Attempt to send message...")
            performSendMessage()
        }
    }

    class ChatMessage(val text:String){

    }
    private fun performSendMessage() {
        //how do we send a message to the firebase

        val text=editText_chat_log.text.toString()


        val reference=FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatMessage=ChatMessage(text)
                reference.setValue(chatMessage)
                        .addOnSuccessListener {
                            Log.d(TAG,"Saved our chat message: ${reference.key}")
                        }
        
    }

    private fun stupDummyData() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ChatFromItem("Hey! How are you?"))

        adapter.add(ChatToItem("Nigh BC"))



        recyclerView_chatlog.adapter=adapter

    }
}

class ChatFromItem(val text:String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text=text

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

}
class ChatToItem(val text:String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text=text

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}
