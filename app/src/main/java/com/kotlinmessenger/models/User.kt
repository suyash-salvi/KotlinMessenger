package com.kotlinmessenger.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//we use parcelable because we want' to access user data from the newmessage activity to the
//latest message activity as a whole
@Parcelize
class User(val uid:String, val username:String,val profileImageUrl:String):Parcelable{
    //no arg constructor
    constructor():this("","","")
}