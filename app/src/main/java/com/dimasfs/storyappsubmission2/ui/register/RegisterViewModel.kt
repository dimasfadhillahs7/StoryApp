package com.dimasfs.storyappsubmission2.ui.register

import androidx.lifecycle.ViewModel
import com.dimasfs.storyappsubmission2.repository.StoryRepository

class RegisterViewModel (private val storyRepository: StoryRepository): ViewModel() {
    fun userRegister(name: String, email: String, password: String) =
        storyRepository.userRegister(name, email, password)
}