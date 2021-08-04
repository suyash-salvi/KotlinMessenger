package com.kotlinmessenger.views

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlinmessenger.R
import com.kotlinmessenger.models.ChatMessage
import com.kotlinmessenger.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_row.view.*

//a class that displays messages on the row
class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>(){
    //there's a chatpartner of the type UserClass
    var ChatPartnerUser: User?=null
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //
        viewHolder.itemView.latest_message_text_view.text=chatMessage.text
        //declare variable
        val chatPartnerId:String
        //
        if(chatMessage.fromId== FirebaseAuth.getInstance().uid){
            //
            chatPartnerId=chatMessage.toId
        }
        //if user is receiving the message
        else{
            //display it from the left
            chatPartnerId=chatMessage.fromId
        }
    //fetch
        val ref= FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               ChatPartnerUser=snapshot.getValue(User::class.java)

                viewHolder.itemView.username_text_view_latest_message_activity.text=ChatPartnerUser?.username
                val targetImageView= viewHolder.itemView.imageview_latest_message
                Picasso.get().load(ChatPartnerUser?.profileImageUrl).into(targetImageView)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
    //reference to the layout
    override fun getLayout(): Int {
        //use latest message layout
        return R.layout.latest_message_row
    }

}