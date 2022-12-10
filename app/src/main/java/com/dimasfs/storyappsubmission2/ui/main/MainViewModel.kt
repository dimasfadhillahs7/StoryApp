package com.dimasfs.storyappsubmission2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.repository.User
import com.dimasfs.storyappsubmission2.response.StoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories(): LiveData<PagingData<StoryItem>> {
        return  storyRepository.getStories().cachedIn(viewModelScope)
    }

    fun getUser(): LiveData<User> {
        return storyRepository.getUserData()
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }
}