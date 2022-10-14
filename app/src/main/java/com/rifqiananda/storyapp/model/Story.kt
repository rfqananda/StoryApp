package com.rifqiananda.storyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "story")
data class Story(

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,
    val createdAt: String,
    val description: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val photoUrl: String
) : Serializable