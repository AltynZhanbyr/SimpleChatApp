package com.example.newchatapp.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.newchatapp.AppKeys
import com.example.newchatapp.R
import com.example.newchatapp.databinding.FragmentRegisterBinding
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


class RegisterFragment : Fragment() {

    private var binding:FragmentRegisterBinding? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)

        return binding?.root
    }

    override fun onStart() {
        super.onStart()

        auth = Firebase.auth

        val viewModelFactory = ViewModelFactory()
        val viewModel = ViewModelProvider(this, viewModelFactory)[UsersViewModel::class.java]

        binding?.saveButton?.setOnClickListener { button->
            val email = binding?.userEmail?.text.toString()
            val password = binding?.userPassword?.text.toString()
            val name = binding?.userFirstName?.text.toString()
            val lastName = binding?.userLastName?.text.toString()

            viewModel.saveUser(email,password,name,lastName)

        }
        viewModel.isRegistrationComplete.observe(viewLifecycleOwner){
            if(it)
                showVerificationText()
            else
                Snackbar.make(binding?.root?.rootView!!, "Registration Failed", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showVerificationText(){
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener(requireActivity()){
            if(it.isSuccessful){
                binding?.registerFormLayout?.visibility = View.GONE
                binding?.verificationText?.visibility = View.VISIBLE
            }
            else{
                Snackbar.make(binding?.root?.rootView!!, "Send email failed",Snackbar.LENGTH_SHORT ).show()
            }
        }
    }
}