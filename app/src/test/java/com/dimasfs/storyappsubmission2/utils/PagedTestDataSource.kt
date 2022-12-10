package com.dimasfs.storyappsubmission2.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dimasfs.storyappsubmission2.response.StoryItem

class PagedTestDataSource :
    PagingSource<Int, LiveData<List<StoryItem>>>() {

    companion object {
        fun snapshot(items: List<StoryItem>): PagingData<StoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}