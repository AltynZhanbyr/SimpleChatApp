package com.example.newchatapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newchatapp.R
import com.example.newchatapp.databinding.MessageItemBinding
import com.example.newchatapp.model.Message

class ChatAdapter(private val messages:MutableList<Message>):RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val binding:MessageItemBinding = MessageItemBinding.bind(view)

        fun bind(message:Message){
            binding.message.text = message.message
            binding.messageSender.text = message.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent,false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    fun addMessage(message: Message){
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}