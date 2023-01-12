package com.example.newchatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newchatapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding? = null
    private lateinit var userDatabase:DatabaseReference
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

                userDatabase.push().setValue(user)
                Snackbar.make(it, "User saved", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}