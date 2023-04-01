package com.dicoding.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("documentation_url")
	val documentationUrl: String? = null,

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<UserItem>
)


