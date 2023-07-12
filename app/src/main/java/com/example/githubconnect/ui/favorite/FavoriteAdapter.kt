package com.example.githubconnect.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubconnect.data.database.FavoriteUser
import com.example.githubconnect.databinding.ItemUserListBinding
import com.example.githubconnect.utils.FavoriteDiffCallBack

class FavoriteAdapter(
    private val onClick: (FavoriteUser) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val listFavoriteUser = ArrayList<FavoriteUser>()
    fun setListFavoriteUser(listFavoriteUser: List<FavoriteUser>) {
        val diffCallback = FavoriteDiffCallBack(this.listFavoriteUser, listFavoriteUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUser.clear()
        this.listFavoriteUser.addAll(listFavoriteUser)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class FavoriteViewHolder(val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavoriteUser.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.binding.apply {
            tvUsername.text = listFavoriteUser[position].username
            tvUserAccountType.text = listFavoriteUser[position].type
            Glide.with(holder.itemView.context)
                .load(listFavoriteUser[position].avatarUrl)
                .into(imgAvatar)
        }
        holder.itemView.setOnClickListener {
            onClick(listFavoriteUser[position])
        }
    }
}
