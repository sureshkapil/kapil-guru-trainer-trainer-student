package com.kapilguru.trainer.student.exam.model

import com.google.gson.annotations.SerializedName

data class StudentSubmitAllQuestionsRequest(
    @SerializedName("response_json") val responseJson: String = "")