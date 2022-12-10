package com.dimasfs.storyappsubmission2.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dimasfs.storyappsubmission2.R
import com.dimasfs.storyappsubmission2.databinding.ActivityLoginBinding
import com.dimasfs.storyappsubmission2.extensions.animateVisibility
import com.dimasfs.storyappsubmission2.repository.User
import com.dimasfs.storyappsubmission2.ui.ViewModelFactory
import com.dimasfs.storyappsubmission2.ui.main.MainActivity
import com.dimasfs.storyappsubmission2.ui.main.MainActivity.Companion.EXTRA_TOKEN
import com.dimasfs.storyappsubmission2.ui.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        factory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        playAnimation()
        clickRegister()
        clickLogin()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 600
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.login, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(500)


        val together = AnimatorSet().apply {
            playTogether(email, password)
        }


        AnimatorSet().apply {
            playSequentially(login, register, together)
            start()
        }
    }

    private fun clickLogin() {
        binding.buttonLogin.setOnClickListener {
            runLoading(true)
            login()
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        loginViewModel.userLogin(email, password).observe(this) {
            it.onSuccess { response ->
                response.loginResult?.token?.let { token ->
                    loginViewModel.saveUser(
                        User(
                            response.loginResult.name.toString(),
                            response.loginResult.token.toString(),
                            true
                        )
                    )
                    loginViewModel.saveToken(token)
                    Intent(this@LoginActivity, MainActivity::class.java).also { intent ->
                        intent.putExtra(EXTRA_TOKEN, token)
                        startActivity(intent)
                        finish()

                    }
                }
                Toast.makeText(this,getString(R.string.login_success),Toast.LENGTH_SHORT).show()
            }
            it.onFailure {
                Snackbar.make(binding.root, getString(R.string.login_error), Snackbar.LENGTH_LONG).show()
                runLoading(false)
            }
        }
    }

    private fun runLoading(b: Boolean) {
        binding.apply {
            etEmail.isEnabled = !b
            etPassword.isEnabled = !b
            buttonLogin.isEnabled = !b

            if (b) {
                loading.animateVisibility(true)
            } else {
                loading.animateVisibility(false)
            }
        }
    }

    private fun clickRegister() {
       binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
       }
    }
}