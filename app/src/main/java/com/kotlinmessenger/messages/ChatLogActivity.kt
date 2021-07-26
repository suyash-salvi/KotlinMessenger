package com.kotlinmessenger.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.kotlinmessenger.NewMessageActivity
import com.kotlinmessenger.R
import com.kotlinmessenger.models.ChatMessage
import com.kotlinmessenger.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import java.lang.Exception

//chatlog activity
class ChatLogActivity : AppCompatActivity() {

    companion object{
        //tag so we can find our logs quickly
        val TAG="Chatlog"

    }
    //creating a single parcelable so that we need not call it multiple times
    var toUser: User?=null

    //created an adapter
    val adapter=GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //layout xml
        setContentView(R.layout.activity_chat_log)

        //
        recyclerView_chatlog.adapter=adapter



        //getting the USER_KEY from New Message Activity
        //val username=intent.getStringExtra(NewMessageActivity.USER_KEY)
        toUser=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        //actionbar title
        supportActionBar?.title=toUser?.username


        //stupDummyData()


        //listen message function
        listenForMessages()


        send_button_chat_log.setOnClickListener {
            Log.d(TAG,"Attempt to send message...")

            performSendMessage()
            //clear the message so that it gives the feeling it has been sent
            editText_chat_log.getText().clear()



        }
    }





    //listen to the messages
    private fun listenForMessages() {
        //get the id of the sender
        val fromId=FirebaseAuth.getInstance().uid
        //get the id of the receiver
        val toId=toUser?.uid
        //get the reference of the database
        //getInstance: get instance of db
        //getReference: get reference of root node for reading and writing
        val ref=FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        //Classes implementing this interface can be used to
        // receive events about changes in the child locations of a given DatabaseReference ref.
        ref.addChildEventListener(object: ChildEventListener{
            //when the child is added to the node
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //
                val chatMessage=snapshot.getValue(ChatMessage::class.java)

                if(chatMessage!=null){
                    Log.d(TAG,chatMessage.text)
                    //if msg sent by the user matches the firebase auth uid
                    if(chatMessage.fromId==FirebaseAuth.getInstance().uid){
                        //load the user in a variable
                        val currentUser=LatestMessagesActivity.currentUser?:return
                        //add the user sent text msg to the layout
                        adapter.add(ChatToItem(chatMessage.text, currentUser))

                    }
                    else{
                        //add the sender sent text msg to the layout
                        adapter.add(ChatFromItem(chatMessage.text, toUser?:return))

                    }



                }
                //this views the messages from the spot we left off and we don't need to scroll
                recyclerView_chatlog.scrollToPosition(adapter.itemCount-1)

            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {


            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        })

    }



     fun performSendMessage() {
        //how do we send a message to the firebase

        //convert the typed message to a string
         val text = editText_chat_log.text.toString()
         //null check for the text
         if(text.isEmpty())return
         //get user instance
         val fromId=FirebaseAuth.getInstance().uid
         //get the other chatpartner user instance
         val user=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
         //get his id
         val toId=user?.uid

         //get the reference to messages in Firebase Database
//         val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
         //
         val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
         //
         val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
         //
         if(fromId==null) {return}

         //we pass all the required values for chatting
         val chatMessage = ChatMessage(reference.key?:return, text, fromId, toId!!,
                 System.currentTimeMillis()/1000 )
         //set reference
         reference.setValue(chatMessage)
                 .addOnSuccessListener {
                     Log.d(TAG, "Saved our chat message: ${reference.key}")

                     recyclerView_chatlog.scrollToPosition(adapter.itemCount-1)
                 }

         //

             toReference.setValue(chatMessage)

         val latestMessageRef=FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
         latestMessageRef.setValue(chatMessage)

         val latestMessageToRef=FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
         latestMessageToRef.setValue(chatMessage)


    }



    /*private fun stupDummyData() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ChatFromItem("Hey! How are you?"))

        adapter.add(ChatToItem("Nigh BC"))



        //recyclerView_chatlog.adapter=adapter

    }*/
}

//
class ChatFromItem(val text:String, val user:User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //
        viewHolder.itemView.textView_from_row.text=text

        //Load our user Image into star
        val uri=user.profileImageUrl
        val targetImageView=viewHolder.itemView.imageView_chat_from_row
        //
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        //
        return R.layout.chat_from_row
    }

}



//
class ChatToItem(val text:String, val user:User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text=text


        //Load our user Image into star
        val uri=user.profileImageUrl
        //
        val targetImageView=viewHolder.itemView.imageView_chat_to_row
        //load uri in imageview
        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {
        //
        return R.layout.chat_to_row
    }
}





