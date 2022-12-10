package com.dimasfs.storyappsubmission2.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.repository.User

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getStoryLocation(token: String) =
        repository.getStoriesLocation(token)

    fun getUser(): LiveData<User> {
        return repository.getUserData()
    }
}