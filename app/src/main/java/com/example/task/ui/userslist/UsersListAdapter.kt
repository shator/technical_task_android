package com.example.task.ui.userslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task.data.models.User
import com.example.task.databinding.UserListItemBinding

class UsersListAdapter(private val onItemSelected: (user: User) -> Unit?) :
    RecyclerView.Adapter<UsersListAdapter.UserViewHolder>() {

    private var users: List<User> = listOf()

    class UserViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, onItemSelected: (user: User) -> Unit?) {
            binding.textViewName.text = user.name
            binding.textViewEmail.text = user.email
            binding.textViewCreation.text = user.created_at

            itemView.setOnLongClickListener {
                onItemSelected(user)
                return@setOnLongClickListener true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position], onItemSelected)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }
}