package com.kapilguru.trainer.student.profile.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.profile.data.StudentProfileData

@Keep
data class StudentProfileResponse(
    @SerializedName("data") val data: List<StudentProfileData> ? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)
