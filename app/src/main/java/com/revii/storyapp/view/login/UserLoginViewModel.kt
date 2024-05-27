package com.revii.storyapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revii.storyapp.data.model.UserAuthModel
import com.revii.storyapp.data.model.UserPreference
import com.revii.storyapp.data.remote.network.NetworkConfig
import com.revii.storyapp.data.remote.payload.LoginPayload
import com.revii.storyapp.data.remote.response.ServerErrorResponse
import com.revii.storyapp.data.remote.response.UserLoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserLoginViewModel(private val pref: UserPreference) : ViewModel() {
    private val _login = MutableLiveData<Boolean>()
    val login: LiveData<Boolean> = _login

    private val _snackbarText = MutableLiveData<String>()
    val snackbarText: LiveData<String> = _snackbarText

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun saveUser(user: UserAuthModel) {
        viewModelScope.launch {
            NetworkConfig.setToken(user.tokenAuth)
            pref.saveUser(user)
        }
    }

    fun login(email: String, password: String) {
        val payload = LoginPayload(email, password)
        val client = NetworkConfig.getApiService().login(payload)
        _isLoading.value = true
        client.enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        val token = responseBody.loginResult?.token as String
                        _login.value = true
                        saveUser(UserAuthModel(token, true))
                        _snackbarText.value = responseBody.message
                    }
                } else {
                    val responseBody = response.errorBody()
                    _login.value = false
                    if (responseBody != null) {
                        val mapper =
                            Gson().fromJson(responseBody.string(), ServerErrorResponse::class.java)
                        _snackbarText.value = mapper.message
                        Log.e(TAG, "onFailure2: ${mapper.message}")
                    } else {
                        _snackbarText.value = response.message()
                        Log.e(TAG, "onFailure2: ${response.message()}")
                    }

                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                _isLoading.value = false
                _login.value = false
                _snackbarText.value = t.message ?: "Error !"
                Log.e(TAG, "onFailure: Gagal, ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}