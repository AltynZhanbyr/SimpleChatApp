package com.example.newchatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newchatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ViewModelFactory(
//    private val firebaseAuth: FirebaseAuth,
//    private val firebaseDatabase: FirebaseDatabase,
//    private val databaseReference: DatabaseReference
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UsersViewModel::class.java))
            return UsersViewModel() as T
        else
            throw IllegalArgumentException("Unknown ViewModel")
    }
}