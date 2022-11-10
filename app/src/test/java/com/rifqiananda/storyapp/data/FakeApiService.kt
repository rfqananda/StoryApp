package com.rifqiananda.storyapp.data

import com.rifqiananda.storyapp.DataDummy
import com.rifqiananda.storyapp.model.FileUploadResponse
import com.rifqiananda.storyapp.model.GetStories
import com.rifqiananda.storyapp.model.Login
import com.rifqiananda.storyapp.model.Register
import com.rifqiananda.storyapp.networking.ApiEndPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class FakeApiService : ApiEndPoint {
    private val dummyResponse = DataDummy.generateDummyNewsResponse()
    override suspend fun getAllStories(token: String, page: Int, size: Int): GetStories {
        return dummyResponse
    }

    override fun getStoriesMap(token: String, location: Int): Call<GetStories> {
        TODO("Not yet implemented")
    }

    override fun createAccount(
        name: String,
        email: String,
        password: String
    ): Call<Register> {
        TODO("Not yet implemented")
    }

    override fun login(email: String, password: String): Call<Login> {
        TODO("Not yet implemented")
    }


    override fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Call<FileUploadResponse> {
        TODO("Not yet implemented")
    }

}