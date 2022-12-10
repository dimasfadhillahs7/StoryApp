package com.dimasfs.storyappsubmission2.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dimasfs.storyappsubmission2.api.ApiService
import com.dimasfs.storyappsubmission2.repository.UserPreference
import com.dimasfs.storyappsubmission2.response.StoryItem
import kotlinx.coroutines.flow.first

class PagingSource(private val apiService: ApiService, private val pref: UserPreference) : PagingSource<Int, StoryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = "Bearer ${pref.getUserData().first().token}"
            val responseData = apiService.getStories(token, page, params.loadSize).body()!!.storyItems

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}