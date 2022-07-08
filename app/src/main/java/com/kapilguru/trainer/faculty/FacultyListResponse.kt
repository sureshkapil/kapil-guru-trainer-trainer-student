package com.kapilguru.trainer.faculty

import com.google.gson.annotations.SerializedName

data class FacultyListResponse(
    @SerializedName("data") val facultyListResponseApi: List<FacultyListResponseApi>?, @SerializedName("message") val message: String? = "", @SerializedName("status") val status: Int? = 0
)