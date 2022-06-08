package com.kapilguru.trainer.studentsList.model

import androidx.annotation.Keep
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

@Keep
data class StudentListApi(
    @SerializedName("allData") val allData: JsonObject?,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)