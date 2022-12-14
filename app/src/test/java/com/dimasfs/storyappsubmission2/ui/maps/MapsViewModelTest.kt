package com.dimasfs.storyappsubmission2.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.repository.User
import com.dimasfs.storyappsubmission2.response.StoriesResponse
import com.dimasfs.storyappsubmission2.ui.main.MainViewModel
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
class MapsViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val token = "kjdnfkwenfwe93244nkrb4jhr43984f4hfb493434fkjr3bf3948434-fef43f8"
    private val name = "Dimas Fadhillah Sugiono"

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `Get Stories Location successfully`() {
        val expectedStory = MutableLiveData<Result<StoriesResponse>>()
        val locationResponse = DataDummy.generateDummyStoriesLocation()
        expectedStory.value = Result.success(locationResponse)

        Mockito.`when`(storyRepository.getStoriesLocation(token)).thenReturn(expectedStory)
        val actualStory = mapsViewModel.getStoryLocation(token).getOrAwaitValue()
        Mockito.verify(storyRepository).getStoriesLocation(token)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result)
    }

    @Test
    fun `Get user is successfully` () {

        val repository = Mockito.mock(StoryRepository::class.java)
        val liveData = MutableLiveData<User>()
        liveData.value = User(name, token, true)
        Mockito.`when`(repository.getUserData()).thenReturn(liveData)

        val mapsViewModel = MainViewModel(repository)
        Assert.assertEquals(mapsViewModel.getUser(), liveData)
    }
}