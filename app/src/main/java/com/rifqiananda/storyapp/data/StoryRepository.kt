package com.rifqiananda.storyapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.rifqiananda.storyapp.database.StoryDatabase
import com.rifqiananda.storyapp.model.Story
import com.rifqiananda.storyapp.networking.ApiEndPoint

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiEndPoint, private val mCtx: Context)  {


    fun getStories(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, mCtx),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

}