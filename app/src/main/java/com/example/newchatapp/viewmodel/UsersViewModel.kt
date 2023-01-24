package com.example.newchatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newchatapp.AppKeys
import com.example.newchatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UsersViewModel:ViewModel() {
    private val firebaseAuth: FirebaseAuth
    private val firebaseDatabase: FirebaseDatabase
    private val databaseReference:DatabaseReference

    var userList = mutableListOf<User>()
    var users = MutableLiveData<MutableList<User>>()

    var isDataDoNotExists = MutableLiveData(false)
    var isRegistrationComplete = MutableLiveData<Boolean>()
    var isSignUpComplete = MutableLiveData<Boolean>()
    var isAccountVerified = MutableLiveData<Boolean>()

    init{
        firebaseAuth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference(AppKeys.USER_KEY)

        getAllUsers()
    }

    fun getAllUsers(){
        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(userList.size>0)
                    userList.clear()
                for(data in p0.children){
                    if(data.getValue(User::class.java)!!.userId!=firebaseAuth.currentUser?.uid)
                        userList.add(data.getValue(User::class.java)!!)
                }
                users.postValue(userList)
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun saveUser(email:String, password:String, name:String, lastName:String){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                val user = firebaseAuth.currentUser
                createUser(user!!,name,lastName)
                isRegistrationComplete.value = true
            }
            else{
                isRegistrationComplete.value = false
            }
        }
    }

    fun signIn(email:String, password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                if(firebaseAuth.currentUser?.isEmailVerified!!) {
                    isSignUpComplete.postValue(true)
                    isAccountVerified.postValue(true)
                }
                else
                    isAccountVerified.postValue(false)
            }
            else
                isSignUpComplete.postValue(false)
        }
    }

    fun signOut(){
        Firebase.auth.signOut()
    }

    private fun createUser(user:FirebaseUser, name:String, lastName:String){
        val newUser = User(user?.uid,name,lastName,user?.email)
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child(newUser.userId.toString()).setValue(newUser).await()
        }
    }
}