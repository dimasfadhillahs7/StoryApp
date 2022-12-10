package com.dimasfs.storyappsubmission2.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dimasfs.storyappsubmission2.di.Injection
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.ui.create.CreateViewModel
import com.dimasfs.storyappsubmission2.ui.login.LoginViewModel
import com.dimasfs.storyappsubmission2.ui.main.MainViewModel
import com.dimasfs.storyappsubmission2.ui.maps.MapsViewModel
import com.dimasfs.storyappsubmission2.ui.register.RegisterViewModel

class ViewModelFactory(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(CreateViewModel::class.java)) {
            return CreateViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unregister view model: " + modelClass.name)
    }



    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}