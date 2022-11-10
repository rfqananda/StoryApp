package com.rifqiananda.storyapp.ui.view

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rifqiananda.storyapp.data.StoryRepository
import com.rifqiananda.storyapp.di.Injection
import com.rifqiananda.storyapp.model.GetStories
import com.rifqiananda.storyapp.model.Login
import com.rifqiananda.storyapp.model.Story
import com.rifqiananda.storyapp.networking.ApiRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel(storyRepository: StoryRepository) : ViewModel(){

    private val api by lazy { ApiRetrofit().endpoint }

    val story: LiveData<PagingData<Story>> =
        storyRepository.getStories().cachedIn(viewModelScope)

    val mapStories = MutableLiveData<ArrayList<Story>>()

    fun setStoriesMap(token : String, location: Int){
        api.getStoriesMap(token, location).enqueue(object : Callback<GetStories>{
            override fun onResponse(call: Call<GetStories>, response: Response<GetStories>) {
                if (response.isSuccessful){
                    mapStories.postValue(response.body()?.listStory)
                }
            }

            override fun onFailure(call: Call<GetStories>, t: Throwable) {
                Log.d("Failure", t.message!!)
            }

        })
    }

    fun getStoriesMap(): LiveData<ArrayList<Story>>{
        return mapStories
    }
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