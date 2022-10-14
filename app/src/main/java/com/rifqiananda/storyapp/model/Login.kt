package com.rifqiananda.storyapp.model

data class Login(
    val error: Boolean,
    val loginResult: LoginResult,
    val message: String
)