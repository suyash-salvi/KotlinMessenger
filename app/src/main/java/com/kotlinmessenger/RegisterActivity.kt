package com.kotlinmessenger

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
//for using objects from activity_main
import kotlinx.android.synthetic.main.register_main.*
import java.util.*


class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //starts with main page
        setContentView(R.layout.register_main)

        //when register button is clicked
        register_button_register.setOnClickListener {
            //go to register function
            performRegister()


        }


        //if click already have an account
        already_have_acount_textview.setOnClickListener {
            //send a log message
            Log.d("MainActivity","Try to show login activity")
            //address the redirect page
            val intent= Intent(this,LoginActivity::class.java)
            //start the redirected page
            startActivity(intent)

        }

        //select photo button
        selectphoto_button_register.setOnClickListener {
            Log.d("MainActivity","Try to show photo selector")
            //ACTION_PICK is to allow a user to select an image from any of the
            // installed apps which registered for such an action.
            // It is not possible to specify from which album to select.
            // It is at the user discretion to decide which app to use and to browse to the desired album to select the photo.
            val intent=Intent(Intent.ACTION_PICK)
            //type of the file to be picked
            intent.type="image/*"
            //carries out the activity for the result
            startActivityForResult(intent,0)
        }
    }

    //initialize a variable without any value
    var selectedPhotoUri: Uri?=null

    //to retrive the result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //
        super.onActivityResult(requestCode, resultCode, data)
        //check req code and the presence of data
        if(requestCode==0&&resultCode==Activity.RESULT_OK&&data!=null){
            //proceed and check what the selected image was
                selectphoto_button_register.text=""
            //success log mesg
            Log.d("RegisterActivity","Photo was selected")
            //stored the retrieved file in the variable
            selectedPhotoUri=data.data
            //store the bitmap image into a variable
            val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            //set the image to the image viewer
            select_photoImage_View.setImageBitmap(bitmap)
            //set the bottom layer image viewer to be transparent
            selectphoto_button_register.alpha=0f

            //val bitmapDrawable=BitmapDrawable(bitmap)
           // selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }
    //when register is pressed
    private fun performRegister(){
        //convert email to string
        val email=edit_text_EmailAddress.text.toString()
        //convert password to string
        val password=edit_text_Password.text.toString()

        //is any one of the the fields is empty
        if(email.isEmpty()||password.isEmpty()){
            //toast message to enter details
            Toast.makeText(this,"Please enter the required details",Toast.LENGTH_SHORT).show()
            //return
        }


        //firebase authentication of a user with an email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                //to be notified of completion
            .addOnCompleteListener{

                if(!it.isSuccessful) return@addOnCompleteListener

                //else if successful
                Log.d("RegisterActivity","uid:${it.result?.user?.uid}")
                uploadImageToFirebaseStorage()

            }
                //if failed
            .addOnFailureListener{
                Log.d("Main","Failed to create user: ${it.message}")
                Toast.makeText(this,"Failed to create user:\n ${it.message}",Toast.LENGTH_SHORT).show()
            }

    }
    //upload image to firebase
     private fun uploadImageToFirebaseStorage() {
        Log.d("RegisterActivity", "Pohochla re baba")
        //check if image is selected
        if (selectedPhotoUri== null){return}
        //any random ID is generated as the filename
            val filename = UUID.randomUUID().toString()
        //the location for storage is mentioned
         val ref = FirebaseStorage.getInstance().getReference("images/$filename")

            //Asynchronously uploads from a content URI to the reference
            ref.putFile(selectedPhotoUri!!)
                    //if success
                .addOnSuccessListener {
                    Log.d("RegisterActivity","Successfully uploaded image ${it.metadata?.path}")

                    //success in Asynchronously retrieving a long lived download URL with a revokable token
                    ref.downloadUrl.addOnSuccessListener {
                        //file location to string
                        it.toString()
                        Log.d("RegisterActivity","File loc:$it")
                        //passing the address to a fun that saves data to the firebase
                        saveUserToFirebase(it.toString())



                    }
                }

        }
    //Saving user info in Firebase DB.(The Url of image is sent as a parameter)
    private fun saveUserToFirebase(profileImageUrl: String){
        Log.d("RegisterActivity","You've reached the saveUserToFirebase fn")
        val uid=FirebaseAuth.getInstance().uid?:""//getting the uid if not null from Auth
        val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")//get users uid
        val user=User(uid,edit_text_Username.text.toString(),profileImageUrl)//create a variable user
        ref.setValue(user)//
                .addOnSuccessListener {
                    //If success
                    Log.d("RegisterActivity","Finally we save the user to db")
                    val intent=Intent(this,LatestMessagesActivity::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                   
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity","${it.message}")
                    Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()

                }


    }




    }

class User(val uid:String, val username:String,val profileImageUrl:String)