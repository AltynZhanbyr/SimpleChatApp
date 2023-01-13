package com.example.newchatapp.model

import android.provider.ContactsContract

data class User(
    val userId:String?=null,
    var userFirstName:String?=null,
    var userLastName:String?=null,
    var userEmail: String?=null,
)
