package com.example.newchatapp.model

data class Message(
    val receiverID:String?=null,
    val senderID:String?=null,
    val message:String?=null,
    val unseen:Boolean?=null,
    val isMine:Boolean?=null
)