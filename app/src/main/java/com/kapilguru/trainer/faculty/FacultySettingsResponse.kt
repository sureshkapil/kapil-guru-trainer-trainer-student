package com.kapilguru.trainer.faculty

import com.google.gson.annotations.SerializedName

data class FacultySettingsResponse(
    @SerializedName("data") val facultySettingsResponseApi: FacultySettingsResponseApi, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)