package com.dimasfs.storyappsubmission2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dimasfs.storyappsubmission2.api.ApiService
import com.dimasfs.storyappsubmission2.paging.PagingSource
import com.dimasfs.storyappsubmission2.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await

class StoryRepository (private val pref: UserPreference, private val apiService: ApiService){
    fun getStories(): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                PagingSource(apiService, pref)
            }
        ).liveData
    }

    fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        try {
            val response = apiService.userLogin(email, password).await()
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    fun userRegister(name: String, email: String, password: String)
            : LiveData<Result<RegisterResponse>> =
        liveData {
            try {
                val response = apiService.userRegister(name, email, password).await()
                emit(Result.success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.failure(e))
            }
        }

    fun createStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): LiveData<Result<CreateResponse>> = liveData {
        try {
            val response = apiService.createStory(token, file, description, lat, lon).await()
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    fun getStoriesLocation(token: String): LiveData<Result<StoriesResponse>> = liveData {
        try {
            val response = apiService.getStoriesWithLocation(token, 1)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    fun getUserData(): LiveData<User> {
        return pref.getUserData().asLiveData()
    }

    suspend fun saveUserData(user: User) {
        pref.saveUserData(user)
    }

    suspend fun saveToken(token: String) {
        pref.saveToken(token)
    }


    suspend fun login() {
        pref.login()
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preferences, apiService)
            }.also { instance = it }
    }
}
