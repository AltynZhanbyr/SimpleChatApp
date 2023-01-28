package com.example.newchatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.newchatapp.R
import com.example.newchatapp.databinding.FragmentChatBinding
import com.example.newchatapp.model.Message
import com.example.newchatapp.view.adapter.ChatAdapter
import com.example.newchatapp.viewmodel.MessagesViewModel
import com.example.newchatapp.viewmodel.UsersViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatFragment : Fragment() {

    private var binding:FragmentChatBinding?=null
    private var messages = mutableListOf<Message>()
    private lateinit var adapter:ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = ChatFragmentArgs.fromBundle(requireArguments()).receiverID
        val name = ChatFragmentArgs.fromBundle(requireArguments()).receiverName
        val senderName = ChatFragmentArgs.fromBundle(requireArguments()).senderName

        val messageViewModel = ViewModelProvider(this)[MessagesViewModel::class.java]
        val usersViewModel = ViewModelProvider(this)[UsersViewModel::class.java]

        messageViewModel.getAllMessages(id,Firebase.auth.currentUser?.uid!!)

        adapter = ChatAdapter(messages)
        binding?.chatList?.adapter = adapter

        messageViewModel.mes.observe(viewLifecycleOwner){
            adapter.addMessage(it)
        }

        binding?.sendMessageButton?.setOnClickListener {
            messageViewModel.sendMessage(Message(
                senderName,
                id,
                Firebase.auth.currentUser?.uid,
                binding?.chatMessageEditText?.text.toString(),
                false,
                false
            )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}