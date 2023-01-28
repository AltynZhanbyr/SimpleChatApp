package com.example.newchatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.newchatapp.AppKeys
import com.example.newchatapp.R
import com.example.newchatapp.databinding.FragmentMainPageBinding
import com.example.newchatapp.model.User
import com.example.newchatapp.view.adapter.UsersAdapter
import com.example.newchatapp.viewmodel.UsersViewModel
import com.example.newchatapp.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class MainPageFragment : Fragment(),UsersAdapter.OnUserClickListener {

    private var binding:FragmentMainPageBinding?= null
    private var adapter:UsersAdapter?=null
    private var currentUserName:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainPageBinding.inflate(inflater)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()

        val viewModelFactory = ViewModelFactory()
        val viewModel = ViewModelProvider(this, viewModelFactory)[UsersViewModel::class.java]

        viewModel.users.observe(viewLifecycleOwner){
            adapter = UsersAdapter(it,this)
            adapter?.notifyDataSetChanged()
            binding?.chatList?.adapter = adapter
        }

        viewModel?.currentUser?.observe(viewLifecycleOwner){
            if(it!=null)
                currentUserName = it.userFirstName
        }

        binding?.signOutButton?.setOnClickListener{
            viewModel.signOut()
            if(Firebase.auth.currentUser==null)
                findNavController().navigate(R.id.action_mainPageFragment_to_loginFragment)
            else
                Snackbar.make(it,"LogOut error", Snackbar.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onUserClickListener(user:User) {
        val id = user.userId
        val name = user.userFirstName

        val action = MainPageFragmentDirections.actionMainPageFragmentToChatFragment(id!!, name!!,currentUserName?:"none")
        findNavController().navigate(action)
    }
}