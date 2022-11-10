package com.rifqiananda.storyapp

import com.rifqiananda.storyapp.model.GetStories
import com.rifqiananda.storyapp.model.Story

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {

        val items: MutableList<Story> = arrayListOf()

        for (i in 0..100) {
            val story = Story(
                "$i",
                "2022-02-22T22:22:22Z",
                "description $i",
                0.0,
                0.0,
                "author $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
            )
            items.add(story)
        }

        return items

    }

    fun generateDummyNewsResponse(): GetStories {
        val storyList = ArrayList<Story>()
        for (i in 0..10) {
            val news = Story(
                "$i",
                "2022-02-22T22:22:22Z",
                "description $i",
                0.0,
                0.0,
                "author $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
            )
            storyList.add(news)
        }
        return GetStories(false, storyList, "Success")
    }

}