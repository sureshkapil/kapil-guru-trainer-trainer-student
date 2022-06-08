package com.kapilguru.trainer.exams.previousQuestionPapersList.model

import com.google.gson.annotations.SerializedName

data class PreviousQuestionPapersListResponse(
    @SerializedName("data") val previousQuestionPaperList: List<PreviousQuestionPapersListData>?,
    val message: String,
    val status: Int
)
