package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class CheckStudentResponseApi(
    @SerializedName("student_id") var studentId: Int? = 0, @SerializedName("outputResponse") var outputResponse: String? = ""
)