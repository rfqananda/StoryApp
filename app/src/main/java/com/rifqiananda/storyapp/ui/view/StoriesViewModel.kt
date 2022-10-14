package com.rifqiananda.storyapp.ui.view

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rifqiananda.storyapp.data.StoryRepository
import com.rifqiananda.storyapp.di.Injection
import com.rifqiananda.storyapp.model.GetStories
import com.rifqiananda.storyapp.model.Story
import com.rifqiananda.storyapp.networking.ApiRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel(storyRepository: StoryRepository) : ViewModel(){

    val story: LiveData<PagingData<Story>> =
        storyRepository.getStories().cachedIn(viewModelScope)
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoriesViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}