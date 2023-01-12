package com.example.newchatapp

import android.provider.ContactsContract

data class User(
    val userId:String,
    var userFirstName:String,
    var userLastName:String,
    var userEmail: String,
)
