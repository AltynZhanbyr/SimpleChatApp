package com.example.newchatapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newchatapp.R
import com.example.newchatapp.databinding.UserItemBinding
import com.example.newchatapp.model.User

class UsersAdapter(private val userList:MutableList<User>):RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    inner class UserViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding:UserItemBinding = UserItemBinding.bind(view)

        fun bind(user:User){
            binding.apply {
                this.userName.text = user.userFirstName+" "+user.userLastName
                this.userStatus.text = "online"

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return this.userList.size
    }
}