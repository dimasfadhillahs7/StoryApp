package com.dimasfs.storyappsubmission2.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.repository.User
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateViewModel (private val storyRepository: StoryRepository) : ViewModel() {
    fun createStory(
        token: String, file: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?) =
        storyRepository.createStory(token, file, description, lat, lon)

    fun getUser(): LiveData<User> {
        return storyRepository.getUserData()
    }

}