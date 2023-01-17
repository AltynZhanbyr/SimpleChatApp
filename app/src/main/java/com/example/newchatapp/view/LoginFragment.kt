package com.example.newchatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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
            if(user!=null && user.isEmailVerified){
                Snackbar.make(binding?.root?.rootView!!,user.email + "is verified" ,Snackbar.LENGTH_SHORT).show()
            }
        }

        binding?.signInButton?.setOnClickListener {button->
            val email = binding?.userEmail?.text.toString()
            val password = binding?.userPassword?.text.toString()
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(requireActivity()){
                    if(it.isSuccessful) {
                        if(auth.currentUser?.isEmailVerified!!){
                            findNavController().navigate(R.id.action_loginFragment_to_mainPageFragment)
                        }
                        else{
                            Snackbar.make(button,"Please, verify your account", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    else
                        Snackbar.make(button,"Please enter your email or password correctly", Snackbar.LENGTH_SHORT).show()
                }
        }
        binding?.signUnButton?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}