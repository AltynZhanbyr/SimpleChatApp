package com.example.newchatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newchatapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding? = null
    private lateinit var userDatabase:DatabaseReference
    private var lastID:String?=null
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        userDatabase = firebaseDatabase.getReference(AppKeys.USER_KEY)

        binding?.apply {
            saveButton.setOnClickListener{
                val name = userFirstName.text.toString()
                val lastName = userLastName.text.toString()
                val email = userEmail.text.toString()
                val id:String? = userDatabase.push().key

                val user = User(id!!,name,lastName,email)

                lastID  = id

                CoroutineScope(Dispatchers.IO).launch{
                    userDatabase.push().setValue(user)
                }
                Snackbar.make(it, "User saved", Snackbar.LENGTH_SHORT).show()
            }
        }

        val valueEventListener = object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for(data in p0.children){
                    val user = data.getValue(User::class.java)
                    if(user?.userId == lastID)
                        binding?.userInfo?.text = user?.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        userDatabase.addValueEventListener(valueEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}