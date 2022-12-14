package com.dimasfs.storyappsubmission2.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.repository.User
import com.dimasfs.storyappsubmission2.response.LoginResponse
import com.dimasfs.storyappsubmission2.utils.DataDummy
import com.dimasfs.storyappsubmission2.utils.MainDispatcherRule
import com.dimasfs.storyappsubmission2.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val loginResponse = DataDummy.generateDummyLoginResponse()
    private val email = "dimasfadhillahs@gmail.com"
    private val name = "Dimas Fadhillahs"
    private val password = "bismillahdiacc"
    private val token = "kjdnfkwenfwe93244nkrb4jhr43984f4hfb493434fkjr3bf3948434-fef43f8"
    private val user  = User(name, token, true )

    @Before
    fun setup(){
        loginViewModel= LoginViewModel(storyRepository)
    }


    @Test
    fun `Login successfully - return not null`(){
        val expectedLiveData = MutableLiveData<Result<LoginResponse>>()
        expectedLiveData.value = Result.success(loginResponse)

        Mockito.`when`(storyRepository.userLogin(email, password)).thenReturn(expectedLiveData)
        val actual = loginViewModel.userLogin(email, password).getOrAwaitValue()

        Mockito.verify(storyRepository).userLogin(email, password)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result)
    }

    @Test
    fun `Save user successfully`(): Unit = runTest {
        loginViewModel.saveUser(user)
        Mockito.verify(storyRepository).saveUserData(user)
    }

    @Test
    fun `Save token successfully`(): Unit= runTest {
        loginViewModel.saveToken(token)
        Mockito.verify(storyRepository).saveToken(token)
    }

    @Test
    fun `Login successfully`(): Unit= runTest {
        loginViewModel.login()
        Mockito.verify(storyRepository).login()
    }
}