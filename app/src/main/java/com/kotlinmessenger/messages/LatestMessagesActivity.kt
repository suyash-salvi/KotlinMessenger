package com.kotlinmessenger.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kotlinmessenger.NewMessageActivity
import com.kotlinmessenger.NewMessageActivity.Companion.USER_KEY
import com.kotlinmessenger.R
import com.kotlinmessenger.RegisterActivity
import com.kotlinmessenger.models.ChatMessage
import com.kotlinmessenger.models.User
import com.kotlinmessenger.views.LatestMessageRow
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessagesActivity : AppCompatActivity() {

    companion object{
        var currentUser: User?=null
        val TAG="LatestMessages"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        recyclerview_latest_messages_activity.adapter=adapter
        recyclerview_latest_messages_activity.addItemDecoration(DividerItemDecoration(this,
        DividerItemDecoration.VERTICAL))


        floatingActionButton_new_message.setOnClickListener {
            //the the user the the new message page
            val intent=Intent(this, NewMessageActivity::class.java)
            startActivity(intent)
        }


        //setItemclickListener on your adapter
        adapter.setOnItemClickListener{item, view->
            Log.d(TAG,"123")
            val intent=Intent(this,ChatLogActivity::class.java)
            //we are missing the chat partner user

            val row= item as LatestMessageRow

            intent.putExtra(USER_KEY,row.ChatPartnerUser)
            startActivity(intent)
        }


        //setUpDummyRows()
        listenForLatestMessages()

        fetchCurrentUser()

        verifyUserIsLoggedIn()
    }


    val latestMessagesMap= HashMap<String, ChatMessage>()


    private fun refreshRecyclerViewMessages(){
        //clear the adapter to showcase latest messages
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }

    }

    private fun listenForLatestMessages() {
        val fromId=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage=snapshot.getValue(ChatMessage::class.java)?: return
                latestMessagesMap[snapshot.key!!]=chatMessage
                refreshRecyclerViewMessages()



            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            //if there's a change in latest activity
                val chatMessage=snapshot.getValue(ChatMessage::class.java)?: return
                latestMessagesMap[snapshot.key!!]=chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }






    //instance property
    val adapter=GroupAdapter<GroupieViewHolder>()
    /*private fun setUpDummyRows() {


        adapter.add(LatestMessageRow())
        adapter.add(LatestMessageRow())
        adapter.add(LatestMessageRow())


    }
*/


    private fun fetchCurrentUser() {
        val uid=FirebaseAuth.getInstance().uid
        val ref= FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser=snapshot.getValue(User::class.java)
                Log.d("LatestMessages","Current USer ${currentUser?.profileImageUrl}")
            }
        })
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