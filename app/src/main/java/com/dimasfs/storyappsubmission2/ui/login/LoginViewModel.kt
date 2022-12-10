package com.dimasfs.storyappsubmission2.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.repository.User
import com.dimasfs.storyappsubmission2.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel (private val storyRepository: StoryRepository) : ViewModel() {
    fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> = storyRepository.userLogin(email, password)


    fun saveUser(user: User) {
        viewModelScope.launch {
            storyRepository.saveUserData(user)
        }
    }

    fun saveToken(token: String){
        viewModelScope.launch {
            storyRepository.saveToken(token)
        }
    }

    fun login() {
        viewModelScope.launch {
            storyRepository.login()
        }
    }


}