package com.revii.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class AuthenticatedLoginResponse(

	@field:SerializedName("loginResult")
	val userLoginResult: UserLoginResult? = null,

	@field:SerializedName("error")
	val hasError: Boolean,

	@field:SerializedName("message")
	val errorMessage: String
) {
	data class UserLoginResult(

		@field:SerializedName("name")
		val userName: String,

		@field:SerializedName("userId")
		val userIdentity: String,

		@field:SerializedName("token")
		val authenticationToken: String
	)
}
