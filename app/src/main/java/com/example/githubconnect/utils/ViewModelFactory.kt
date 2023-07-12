package com.example.githubconnect.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubconnect.ui.MainViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val preferences: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown View Model: " + modelClass.name)
    }
}
