package com.example.newchatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newchatapp.AppKeys
import com.example.newchatapp.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessagesViewModel: ViewModel() {
    private val firebaseAuth: FirebaseAuth = Firebase.auth
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference(AppKeys.MESSAGE_KEY)

    val messages = mutableListOf<Message>()
    val isMessageAdded = MutableLiveData<Boolean>()

    val mes = MutableLiveData<Message>()


    fun getAllMessages(receiverID:String,senderID:String){
        databaseReference.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message: Message = p0.getValue(Message::class.java)!!
                if (message?.receiverID.equals(receiverID) && message?.senderID.equals(senderID)
                    || message?.receiverID.equals(senderID) && message?.senderID.equals(receiverID)
                ) {
                    mes.value = message
                }
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun sendMessage(message: Message){
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.push().setValue(message)
        }
    }
}