package com.revii.storyapp.view.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.revii.storyapp.data.model.UserAuthModel
import com.revii.storyapp.data.model.UserPreference

class WelcomeViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserAuthModel> {
        return pref.getUser().asLiveData()
    }
}