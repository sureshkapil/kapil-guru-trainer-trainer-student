package com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model

import com.google.gson.annotations.SerializedName

data class QuestionsListData(
    @SerializedName("question_paper") val questionPaper : String?,
    @SerializedName("questions") val questions : String?, )