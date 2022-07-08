package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class CheckStudentResponse(
    @SerializedName("data") val data: CheckStudentResponseApi, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)