package com.revii.storyapp.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.revii.storyapp.R
import com.revii.storyapp.data.model.UserPreference
import com.revii.storyapp.databinding.ActivityLoginBinding
import com.revii.storyapp.utils.dataStore
import com.revii.storyapp.view.ViewModelFactory
import com.revii.storyapp.view.main.MainActivity
import com.revii.storyapp.view.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val userLoginViewModel by viewModels<UserLoginViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnSignIn.isEnabled = s.toString().length >= 8
            }
        })
    }


    private fun setupViewModel() {
        userLoginViewModel.login.observe(this) { isSuccess ->
            if (isSuccess) {
                MainActivity.start(this)
                finish()
            }
        }

        userLoginViewModel.snackbarText.observe(this) { text ->
            when {
                text.contains("Invalid password") -> {
                    binding.edLoginPassword.error =
                        getString(R.string.invalid_password)
                    binding.edLoginPassword.requestFocus()
                }
                text.contains("must be a valid email") -> {
                    binding.edLoginEmail.error =
                        getString(R.string.email_must_valid)
                    binding.edLoginEmail.requestFocus()
                }
                text.contains("success") -> {
                    // Dd Nothing
                }
                text.contains("User not found") -> Snackbar.make(binding.root, getString(R.string.user_not_found), Snackbar.LENGTH_SHORT).show()
                else -> Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
            }
        }

        userLoginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(value: Boolean) {
        with(binding) {
            btnSignIn.isInvisible = value
            btnSignUp.isEnabled = !value
            pbLoadingScreen.isVisible = value
        }

    }

    private fun setupAction() {
        with(binding) {
            btnSignUp.setOnClickListener {
                RegisterActivity.start(this@LoginActivity)
            }


            btnSignIn.setOnClickListener {
                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()
                when {
                    email.isEmpty() -> {
                        binding.edLoginEmail.error = "Masukkan email"
                    }
                    password.isEmpty() -> {
                        binding.edLoginPassword.error = "Masukkan password"
                    }
                    else -> {
                        with(binding) {
                            edLoginPassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
                            edLoginPassword.clearFocus()
                            edLoginEmail.clearFocus()
                        }
                        userLoginViewModel.login(email, password)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            context.startActivity(starter)
        }
    }
}