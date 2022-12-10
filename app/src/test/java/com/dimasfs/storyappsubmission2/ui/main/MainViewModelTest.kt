package com.dimasfs.storyappsubmission2.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dimasfs.storyappsubmission2.paging.PagingAdapter
import com.dimasfs.storyappsubmission2.repository.StoryRepository
import com.dimasfs.storyappsubmission2.response.StoryItem
import com.dimasfs.storyappsubmission2.utils.DataDummy
import com.dimasfs.storyappsubmission2.utils.MainDispatcherRule
import com.dimasfs.storyappsubmission2.utils.PagedTestDataSource
import com.dimasfs.storyappsubmission2.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `Get stories successfully - return not null`() = runTest {
        val storiesResponse = DataDummy.generateDummyStoriesResponse()
        val data: PagingData<StoryItem> = PagedTestDataSource.snapshot(storiesResponse)
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()

        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStory: PagingData<StoryItem> =
            mainViewModel.getStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = PagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(storiesResponse, differ.snapshot())
        assertEquals(storiesResponse.size, differ.snapshot().size)
        assertEquals(storiesResponse[0].name, differ.snapshot()[0]?.name)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}