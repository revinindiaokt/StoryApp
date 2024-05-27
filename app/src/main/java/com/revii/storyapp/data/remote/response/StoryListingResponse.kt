package com.revii.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class StoryListingResponse(

	@field:SerializedName("listStory")
	val storyList: List<StoryItem>? = null,

	@field:SerializedName("error")
	val hasError: Boolean,

	@field:SerializedName("message")
	val errorMessage: String? = null
)
