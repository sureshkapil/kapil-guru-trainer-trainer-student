package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class SignedUpStudentsListResponse(
    @SerializedName("data") val signedUpStudentsListResponseApi: List<SignedUpStudentsListResponseApi>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)