package com.rifqiananda.storyapp.ui.view

import androidx.lifecycle.ViewModel
import com.rifqiananda.storyapp.data.RemoteRepository

class LoginViewModel(private var repository: RemoteRepository = RemoteRepository()) : ViewModel() {

    fun login(username: String, pass: String) = repository.doLogin(username, pass)
}