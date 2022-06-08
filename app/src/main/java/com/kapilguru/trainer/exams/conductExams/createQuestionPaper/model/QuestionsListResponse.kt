package com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model

import com.google.gson.annotations.SerializedName

data class QuestionsListResponse(
    @SerializedName("allData") val questionsListData: QuestionsListData?,
    val message: String,
    val status: Int
)
