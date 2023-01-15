package com.example.newchatapp.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.newchatapp.AppKeys
import com.example.newchatapp.R
import com.example.newchatapp.databinding.FragmentRegisterBinding
import com.example.newchatapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {

    private var binding:FragmentRegisterBinding? = null

    private lateinit var userDatabase: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)

        firebaseDatabase = FirebaseDatabase.getInstance()
        userDatabase =firebaseDatabase.getReference(AppKeys.USER_KEY)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()

        auth = Firebase.auth

        binding?.saveButton?.setOnClickListener { button->
            val email = binding?.userEmail?.text.toString()
            val password = binding?.userPassword?.text.toString()
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(requireActivity()){
                    if(it.isSuccessful){
                        val user = auth.currentUser
                        createUser(user!!)
                        showVerificationText()
                        auth.signOut()
                    }else
                        Snackbar.make(button,"Error", Snackbar.LENGTH_SHORT).show()
                }
        }
    }

    private fun createUser(user: FirebaseUser){
        val newUser = User(user.uid,binding?.userFirstName?.text.toString(),binding?.userLastName?.text.toString(),user.email)
        userDatabase.push().setValue(newUser)
    }
    private fun showVerificationText(){
        binding?.registerFormLayout?.visibility = View.GONE
        binding?.verificationText?.visibility = View.VISIBLE
    }
}