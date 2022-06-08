package com.kapilguru.trainer.exams.previousQuestionsList.model

import com.google.gson.annotations.SerializedName

data class PreviousQuestionsListResponse(
    @SerializedName("data") val previousQuestionsList: List<PreviousQuestionData>?,
    val message: String,
    val status: Int
)
