package com.dimasfs.storyappsubmission2.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.response.RegisterResponse
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
class RegisterViewModelTest{
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val registerResponse = DataDummy.generateDummyRegisterResponse()
    private val name = "Dimas Fadhillahs"
    private val email = "dimasfadhillahs@gmail.com"
    private val password = "bismillahdiacc"

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @Test
    fun `Register successfully - return respone not null`() {
        val expected = MutableLiveData<Result<RegisterResponse>>()
        expected.value = Result.success(registerResponse)
        Mockito.`when`(storyRepository.userRegister(name, email, password)).thenReturn(expected)

        val actual = registerViewModel.userRegister(name, email, password).getOrAwaitValue()
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result)

    }

}