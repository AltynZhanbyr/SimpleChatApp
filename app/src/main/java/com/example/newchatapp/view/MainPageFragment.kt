package com.example.newchatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.newchatapp.AppKeys
import com.example.newchatapp.R
import com.example.newchatapp.databinding.FragmentMainPageBinding
import com.example.newchatapp.model.User
import com.example.newchatapp.view.adapter.UsersAdapter
import com.example.newchatapp.viewmodel.UsersViewModel
import com.example.newchatapp.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class MainPageFragment : Fragment() {

    private var binding:FragmentMainPageBinding?= null
    private var adapter:UsersAdapter?=null
    private lateinit var userDatabase: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private val userList:MutableList<User> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainPageBinding.inflate(inflater)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()

        firebaseDatabase = FirebaseDatabase.getInstance()
        userDatabase = firebaseDatabase.getReference(AppKeys.USER_KEY)
        auth = Firebase.auth

        val viewModelFactory = ViewModelFactory(auth,firebaseDatabase,userDatabase)
        val viewModel = ViewModelProvider(this, viewModelFactory)[UsersViewModel::class.java]

        viewModel.users.observe(viewLifecycleOwner){
            adapter = UsersAdapter(it)
            adapter?.notifyDataSetChanged()
            binding?.chatList?.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}