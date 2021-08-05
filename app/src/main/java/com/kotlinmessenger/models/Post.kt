package com.kotlinmessenger.models

class Post(val id:String,val postText:String,val profileImageUrl:String,
           val timeStamp:Long) {
    constructor():this("","","",-1)
}