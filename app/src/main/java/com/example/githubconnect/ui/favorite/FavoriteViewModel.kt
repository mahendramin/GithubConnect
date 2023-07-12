package com.example.githubconnect.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubconnect.data.database.FavoriteUser
import com.example.githubconnect.data.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository = FavoriteRepository(application)

    fun getAllListFavoriteUser(): LiveData<List<FavoriteUser>> =
        mFavoriteRepository.getAllFavoriteUser()

    fun getFavoriteUser(username: String): LiveData<FavoriteUser> =
        mFavoriteRepository.getUser(username)

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteRepository.delete(favoriteUser)
    }
}
