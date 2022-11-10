package com.rifqiananda.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rifqiananda.storyapp.model.FileUploadResponse
import com.rifqiananda.storyapp.model.Login
import com.rifqiananda.storyapp.model.Register
import com.rifqiananda.storyapp.networking.ApiEndPoint
import com.rifqiananda.storyapp.networking.ApiRetrofit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteRepository(private var apiClient: ApiEndPoint = ApiRetrofit().endpoint) {

    fun doLogin(username: String, pass: String): LiveData<Login> {
        val login = MutableLiveData<Login>()
        apiClient.login(username, pass).enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if (response.isSuccessful) {
                    login.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.e("Failure", t.message!!)
            }

        })

        return login
    }

    fun doRegister(name: String, email: String, password: String): LiveData<Register>{
        val register = MutableLiveData<Register>()
        apiClient.createAccount(name, email, password).enqueue(object : Callback<Register>{
            override fun onResponse(call: Call<Register>, response: Response<Register>) {
                if (response.isSuccessful) {
                    register.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                Log.e("Failure", t.message!!)
            }

        })
        return register
    }

    fun doUpload(token: String, image: MultipartBody.Part, description: RequestBody): LiveData<FileUploadResponse>{
        val upload = MutableLiveData<FileUploadResponse>()

        apiClient.uploadImage(token, image, description).enqueue(object : Callback<FileUploadResponse>{
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if (response.isSuccessful) {
                    upload.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.e("Failure", t.message!!)
            }

        })

        return upload
    }

}