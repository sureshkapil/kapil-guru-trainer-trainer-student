package com.kapilguru.trainer.student.exam.model


import com.google.gson.annotations.SerializedName

data class StudentQuestionsApi(
    @SerializedName("question_paper") var questionPaper: String?,
    @SerializedName("questions") var questions: String?,
    @SerializedName("responses") var responses: String?,
    @SerializedName("summary") var summary: String?
)

