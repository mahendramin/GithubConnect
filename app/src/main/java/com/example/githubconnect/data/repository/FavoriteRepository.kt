package com.example.githubconnect.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubconnect.data.database.FavoriteDao
import com.example.githubconnect.data.database.FavoriteRoomDatabase
import com.example.githubconnect.data.database.FavoriteUser
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteDao.getAllFavoriteUser()

    fun getUser(username: String): LiveData<FavoriteUser> =
        mFavoriteDao.getFavoriteUserByUsername(username)

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.delete(favoriteUser) }
    }
}
