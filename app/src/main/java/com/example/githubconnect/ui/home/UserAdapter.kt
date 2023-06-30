package com.example.githubconnect.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubconnect.data.remote.response.ItemsItem
import com.example.githubconnect.databinding.ItemUserListBinding

class UserAdapter(
    private val listUser: List<ItemsItem>,
    private val onClick: (ItemsItem) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    inner class UserViewHolder(val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.apply {
            tvUsername.text = listUser[position].username
            tvUserAccountType.text = listUser[position].type
            Glide.with(holder.itemView.context)
                .load(listUser[position].avatarUrl)
                .into(imgAvatar)
        }
        holder.itemView.setOnClickListener {
            onClick(listUser[position])
        }
    }
}