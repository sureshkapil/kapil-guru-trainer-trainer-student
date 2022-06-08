package com.kapilguru.trainer.studentExamBatchResult

import com.google.gson.annotations.SerializedName

data class StudentAnswerSheetResponse(
    @SerializedName("summary")
    val summary: String = "",
    @SerializedName("question_paper")
    val questionPaper: String = "",
    @SerializedName("questions_responses")
    val questionsResponses: String = ""
)