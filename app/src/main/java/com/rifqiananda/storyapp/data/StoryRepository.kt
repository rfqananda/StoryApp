package com.rifqiananda.storyapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rifqiananda.storyapp.database.StoryDatabase
import com.rifqiananda.storyapp.model.Story
import com.rifqiananda.storyapp.networking.ApiEndPoint

class StoryRepository(private val quoteDatabase: StoryDatabase, private val apiService: ApiEndPoint, private val mCtx: Context)  {

    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, mCtx)
            }
        ).liveData
    }
}