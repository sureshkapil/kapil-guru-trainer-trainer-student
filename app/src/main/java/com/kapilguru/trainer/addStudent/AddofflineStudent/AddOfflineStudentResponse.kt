package com.kapilguru.trainer.addStudent.AddofflineStudent

import com.google.gson.annotations.SerializedName

data class AddOfflineStudentResponse(
    @SerializedName("data") var addOfflineStudentResponseApi: AddOfflineStudentResponseApi?, @SerializedName("message") var message: String? = "", @SerializedName("status") var status: Int? = 0
)