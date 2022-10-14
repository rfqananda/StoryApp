package com.rifqiananda.storyapp.model

data class GetStories(
    val error: Boolean,
    val listStory: ArrayList<Story>,
    val message: String
)