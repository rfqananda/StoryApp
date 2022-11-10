package com.rifqiananda.storyapp.ui.view

import androidx.lifecycle.ViewModel
import com.rifqiananda.storyapp.data.RemoteRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private var repository: RemoteRepository = RemoteRepository()): ViewModel() {

    fun upload(token: String, image: MultipartBody.Part, description: RequestBody) = repository.doUpload(token, image, description)
}