package com.revii.storyapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.revii.storyapp.data.model.UserPreference
import com.revii.storyapp.view.login.UserLoginViewModel
import com.revii.storyapp.view.main.MainViewModel
import com.revii.storyapp.view.welcome.WelcomeViewModel

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(UserLoginViewModel::class.java) -> {
                UserLoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}