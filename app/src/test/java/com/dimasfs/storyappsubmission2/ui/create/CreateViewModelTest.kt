package com.dimasfs.storyappsubmission2.ui.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.response.CreateResponse
import com.dimasfs.storyappsubmission2.utils.DataDummy
import com.dimasfs.storyappsubmission2.utils.getOrAwaitValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class CreateViewModelTest{
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var createViewModel: CreateViewModel
    private val createResponse = DataDummy.generateDummyCreateResponse()
    private val token = "kjdnfkwenfwe93244nkrb4jhr43984f4hfb493434fkjr3bf3948434-fef43f8"
    private var lat: RequestBody? = null
    private var lon: RequestBody? = null



    @Before
    fun setUp() {
        createViewModel = CreateViewModel(storyRepository)
    }

    @Test
    fun `Create story successfully`() {
        val description = "description".toRequestBody("text/plain".toMediaType())
        val file = Mockito.mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val expectedStory = MutableLiveData<Result<CreateResponse>>()
        expectedStory.value = Result.success(createResponse)
        Mockito.`when`(storyRepository.createStory(token, imageMultipart, description, lat, lon)).thenReturn(
            expectedStory
        )
        (expectedStory)

        val actualStory = createViewModel.createStory(token, imageMultipart, description, lat, lon)
            .getOrAwaitValue()

        Mockito.verify(storyRepository).createStory(token, imageMultipart, description, lat, lon)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result)
    }
}