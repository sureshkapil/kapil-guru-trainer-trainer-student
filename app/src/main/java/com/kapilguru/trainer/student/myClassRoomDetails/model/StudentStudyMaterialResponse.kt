package com.kapilguru.trainer.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName

data class StudentStudyMaterialResponse(
    @SerializedName("data") var studentStudyMaterialResponseApi: List<StudentStudyMaterialResponseApi>?=null,
    @SerializedName("message") var message: String? = "",
    @SerializedName("status") var status: Int? = 0
)