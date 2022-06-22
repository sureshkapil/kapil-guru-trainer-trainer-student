package com.kapilguru.trainer.student.profile.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.profile.data.SaveStudentProfileResData

data class SaveStudentProfileResponse(
    @SerializedName("data") val data: SaveStudentProfileResData,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)
