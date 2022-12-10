package com.dimasfs.storyappsubmission2.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dimasfs.storyappsubmission2.api.ApiConfig
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.repository.UserPreference

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storyapp")
object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }

}