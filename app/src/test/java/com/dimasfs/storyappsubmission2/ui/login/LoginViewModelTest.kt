package com.dimasfs.storyappsubmission2.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.response.LoginResponse
import com.dimasfs.storyappsubmission2.utils.DataDummy
import com.dimasfs.storyappsubmission2.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val loginResponse = DataDummy.generateDummyLoginResponse()
    private val email = "dimasfadhillahs@gmail.com"
    private val password = "bismillahdiacc"

    @Before
    fun setup(){
        loginViewModel= LoginViewModel(storyRepository)
    }

    @Test
    fun `login successfully - return not null`(){
        val expectedLiveData = MutableLiveData<Result<LoginResponse>>()
        expectedLiveData.value = Result.success(loginResponse)

        Mockito.`when`(storyRepository.userLogin(email, password)).thenReturn(expectedLiveData)
        val actual = loginViewModel.userLogin(email, password).getOrAwaitValue()

        Mockito.verify(storyRepository).userLogin(email, password)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result)
    }
}