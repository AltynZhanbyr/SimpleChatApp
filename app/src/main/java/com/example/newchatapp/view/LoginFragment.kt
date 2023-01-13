package com.example.newchatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newchatapp.AppKeys
import com.example.newchatapp.R
import com.example.newchatapp.databinding.FragmentLoginBinding
import com.example.newchatapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var binding:FragmentLoginBinding?=null

    private lateinit var userDatabase:DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        firebaseDatabase = FirebaseDatabase.getInstance()
        userDatabase = firebaseDatabase.getReference(AppKeys.USER_KEY)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()

        auth = Firebase.auth
        if(auth!=null){
            val user = auth.currentUser
            Snackbar.make(binding?.root?.rootView!!, user?.email.toString(), Snackbar.LENGTH_SHORT).show()
            auth.signOut()
        }

        binding?.signInButton?.setOnClickListener {button->
            val email = binding?.userEmail?.text.toString()
            val password = binding?.userPassword?.text.toString()
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(requireActivity()){
                    if(it.isSuccessful)
                        Snackbar.make(button,"Signed successfully", Snackbar.LENGTH_SHORT).show()
                    else
                        Snackbar.make(button,"Error", Snackbar.LENGTH_SHORT).show()
                }
        }
        binding?.signUnButton?.setOnClickListener { button->
            val email = binding?.userEmail?.text.toString()
            val password = binding?.userPassword?.text.toString()
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(requireActivity()){
                    if(it.isSuccessful){
                        val user = auth.currentUser
                        createUser(user!!)
                        Snackbar.make(button,"Registration is successfully", Snackbar.LENGTH_SHORT).show()
                    }else
                        Snackbar.make(button,"Error", Snackbar.LENGTH_SHORT).show()
                }
        }
    }

    private fun createUser(user:FirebaseUser){
        val newUser = User(user.uid,"Test1","Test1",user.email)
        userDatabase.push().setValue(newUser)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}