package com.example.githubconnect.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    var username: String = String(),

    var avatarUrl: String = String(),

    var type: String = String()
)
