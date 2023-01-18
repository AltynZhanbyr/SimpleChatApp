package com.example.newchatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.newchatapp.AppKeys
import com.example.newchatapp.R
import com.example.newchatapp.databinding.FragmentLoginBinding
import com.example.newchatapp.model.User
import com.example.newchatapp.viewmodel.UsersViewModel
import com.example.newchatapp.viewmodel.ViewModelFactory
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

        val viewModelFactory = ViewModelFactory()
        val viewModel = ViewModelProvider(this, viewModelFactory)[UsersViewModel::class.java]

        auth = Firebase.auth
        if(auth!=null){
            val user = auth.currentUser
            if(user!=null && user.isEmailVerified){

            }
        }

        binding?.signInButton?.setOnClickListener {button->
            val email = binding?.userEmail?.text.toString()
            val password = binding?.userPassword?.text.toString()
            viewModel.signIn(email,password)

        }

        viewModel.isSignUpComplete.observe(viewLifecycleOwner){
            if(it)
                findNavController().navigate(R.id.action_loginFragment_to_mainPageFragment)
            else
                Snackbar.make(binding?.root?.rootView!!, "Enter your email or password correct", Snackbar.LENGTH_SHORT).show()
        }
        viewModel.isAccountVerified.observe(viewLifecycleOwner){
            if(!it)
                Snackbar.make(binding?.root?.rootView!!,"Please, verify your email address",Snackbar.LENGTH_SHORT).show()
        }

        binding?.signUpButton?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}