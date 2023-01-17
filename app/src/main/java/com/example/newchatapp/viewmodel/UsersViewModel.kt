package com.example.newchatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newchatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UsersViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val databaseReference:DatabaseReference
):ViewModel() {
    var users = MutableLiveData<MutableList<User>>()
    var isDataDoNotExists = MutableLiveData(false)

    init{
        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(users?.value!=null){
                    if(users?.value?.size!!>0)
                        users?.value?.clear()
                }
                for(data in p0.children){
                    users.value?.add(data.getValue(User::class.java)!!)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                isDataDoNotExists.postValue(true)
            }
        })
    }

    fun getAllUsers(){
    }
}