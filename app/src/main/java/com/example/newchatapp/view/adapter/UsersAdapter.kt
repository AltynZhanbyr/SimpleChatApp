package com.example.newchatapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newchatapp.R
import com.example.newchatapp.databinding.UserItemBinding
import com.example.newchatapp.model.User

class UsersAdapter(private val userList:MutableList<User>,private val onUserClickListener: OnUserClickListener):RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    interface OnUserClickListener{
        fun onUserClickListener(position: Int)
    }


    inner class UserViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding:UserItemBinding = UserItemBinding.bind(view)

        fun bind(user:User, position: Int){
            binding.apply {
                this.userName.text = user.userFirstName+" "+user.userLastName
                this.userStatus.text = "online"
            }
            itemView.setOnClickListener {
                onUserClickListener.onUserClickListener(position =position )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user, position)
    }

    override fun getItemCount(): Int {
        return this.userList.size
    }
}