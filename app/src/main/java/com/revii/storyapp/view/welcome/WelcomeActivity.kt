package com.revii.storyapp.view.welcome

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.revii.storyapp.data.model.UserPreference
import com.revii.storyapp.databinding.ActivityWelcomeBinding
import com.revii.storyapp.utils.AppConstants
import com.revii.storyapp.utils.dataStore
import com.revii.storyapp.view.ViewModelFactory
import com.revii.storyapp.view.login.LoginActivity
import com.revii.storyapp.view.main.MainActivity
import kotlinx.coroutines.*

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val activityScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val welcomeViewModel by viewModels<WelcomeViewModel> {
            ViewModelFactory(
                UserPreference.getInstance(dataStore)
            )
        }

        var isLogin = false

        welcomeViewModel.getUser().observe(this) { model ->
            isLogin = if(model.isLogin) {
                UserPreference.setToken(model.tokenAuth)
                true
            } else {
                false
            }
        }

        activityScope.launch {
            delay(AppConstants.DELAY_SPLASH_SCREEN)
            runOnUiThread {
                if(isLogin) {
                    MainActivity.start(this@WelcomeActivity)
                } else {
                    LoginActivity.start(this@WelcomeActivity)
                }
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.coroutineContext.cancelChildren()
    }
}