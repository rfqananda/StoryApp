package com.rifqiananda.storyapp.ui.view

import androidx.lifecycle.ViewModel
import com.rifqiananda.storyapp.data.RemoteRepository

class RegisterViewModel(private var repository: RemoteRepository = RemoteRepository()): ViewModel() {

    fun register(username: String, email: String, pass: String) = repository.doRegister(username, email, pass)
}