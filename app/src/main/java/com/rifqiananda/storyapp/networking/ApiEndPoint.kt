package com.rifqiananda.storyapp.networking

import com.rifqiananda.storyapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndPoint {

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token : String,
        @Query("page") page: Int,
        @Query("size") size: Int): List<Story>

    @FormUrlEncoded
    @POST("register")
    fun createAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String) : Call<Submit>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String) : Call<Login>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token : String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

}