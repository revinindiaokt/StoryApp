package com.revii.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class StoryPostingResponse(

	@field:SerializedName("error")
	val hasError: Boolean,

	@field:SerializedName("message")
	val errorMessage: String
)
