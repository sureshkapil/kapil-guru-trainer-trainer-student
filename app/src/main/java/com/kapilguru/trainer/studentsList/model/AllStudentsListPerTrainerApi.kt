package com.kapilguru.trainer.studentsList.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AllStudentsListPerTrainerApi(
    @SerializedName("data")
    val studentDetails: List<StudentDetails>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)