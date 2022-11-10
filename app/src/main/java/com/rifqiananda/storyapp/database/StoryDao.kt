package com.rifqiananda.storyapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rifqiananda.storyapp.model.Story

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: List<Story>)

    @Query("SELECT * FROM story ORDER BY createdAt DESC")
    fun getAllStory(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    fun deleteAll()
}