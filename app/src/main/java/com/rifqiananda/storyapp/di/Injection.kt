package com.rifqiananda.storyapp.di

import android.content.Context
import com.rifqiananda.storyapp.data.StoryRepository
import com.rifqiananda.storyapp.database.StoryDatabase
import com.rifqiananda.storyapp.networking.ApiRetrofit

object Injection {

    private val api by lazy { ApiRetrofit().endpoint }

    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = api
        return StoryRepository(database, apiService, context)
    }
}