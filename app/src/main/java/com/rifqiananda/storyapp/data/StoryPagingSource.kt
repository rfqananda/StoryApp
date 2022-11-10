package com.rifqiananda.storyapp.data

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rifqiananda.storyapp.helper.Constant
import com.rifqiananda.storyapp.helper.PreferencesHelper
import com.rifqiananda.storyapp.model.Story
import com.rifqiananda.storyapp.networking.ApiEndPoint

class StoryPagingSource (private val apiService: ApiEndPoint, private val mCtx: Context) : PagingSource<Int, Story>() {

    lateinit var sharedPref: PreferencesHelper

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {

        sharedPref = PreferencesHelper(mCtx)
        val token = sharedPref.getString(Constant.PREF_TOKEN)
        val bearer = "Bearer $token"
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllStories(bearer, position, params.loadSize)
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}