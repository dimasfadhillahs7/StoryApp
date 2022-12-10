package com.dimasfs.storyappsubmission2.utils

import com.dimasfs.storyappsubmission2.response.*

object DataDummy {

    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            "Dimas Fadhillahs",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVhOa2gyeWh1MUVUYThXdnQiLCJpYXQiOjE2Njk5NTE5OTN9.80o9iE2PH3Yup4dIDz2YAlOmzwcZ30Ib9kUNGBXYxFM"
        )

        return LoginResponse(
            false,
            "success",
            loginResult
        )
    }

    fun generateDummyRegisterResponse() : RegisterResponse = RegisterResponse (false, "success")

    fun generateDummyCreateResponse() :CreateResponse = CreateResponse(false,"success")

    fun generateDummyStoriesResponse(): List<StoryItem> {
        val item = arrayListOf<StoryItem>()

        for (i in 0 until 10) {
            val stories = StoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1670647797352_k_LVHhoM.jpg",
                "2022-12-01",
                "Dimas Fadhillahs",
                "Description",
                106.6467,
                -6.1335,
                "story-x4wzdppxdnG6mmVl"

            )
            item.add(stories)
        }
        return item
    }

    fun generateDummyStoriesLocation(): StoriesResponse {
        val item: MutableList<StoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = StoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1670647797352_k_LVHhoM.jpg",
                "2022-12-01",
                "Dimas Fadhillahs",
                "Description",
                106.6467,
                -6.1335,
                "story-x4wzdppxdnG6mmVl"
            )
            item.add(story)
        }
        return StoriesResponse(
            item,
            false,
            "success"

        )
    }


}