package com.dimasfs.storyappsubmission2.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dimasfs.storyappsubmission2.R
import com.dimasfs.storyappsubmission2.databinding.ActivityRegisterBinding
import com.dimasfs.storyappsubmission2.extensions.animateVisibility
import com.dimasfs.storyappsubmission2.ui.ViewModelFactory
import com.dimasfs.storyappsubmission2.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var registerViewModel: RegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        factory = ViewModelFactory.getInstance(this)
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        clickRegister()
        clickLogin()
    }

    private fun clickRegister() {
        binding.buttonRegister.setOnClickListener{
            runLoading(true)
            register()
        }
    }

    private fun register() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        registerViewModel.userRegister(name, email, password).observe(this){
            it.onSuccess {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
            }
            it.onFailure {
                Snackbar.make(binding.root, getString(R.string.registration_error), Snackbar.LENGTH_LONG).show()
                runLoading(false)
            }

        }
    }

    private fun runLoading(b: Boolean) {
        binding.apply {
            etName.isEnabled = !b
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

    private fun clickLogin() {
        binding.buttonLogin.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }
}