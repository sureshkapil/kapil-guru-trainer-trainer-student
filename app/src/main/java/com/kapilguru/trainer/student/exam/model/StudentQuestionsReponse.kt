package com.kapilguru.trainer.student.exam.model


import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.exam.model.StudentQuestionsApi

data class StudentQuestionsReponse(
    @SerializedName("allData") var studentQuestionsApi: StudentQuestionsApi?,
    @SerializedName("message") var message: String?,
    @SerializedName("status") var status: Int?
)