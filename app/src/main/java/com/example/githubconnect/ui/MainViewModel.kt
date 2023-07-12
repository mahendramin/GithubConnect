package com.example.githubconnect.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubconnect.utils.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> = preferences.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }
}
