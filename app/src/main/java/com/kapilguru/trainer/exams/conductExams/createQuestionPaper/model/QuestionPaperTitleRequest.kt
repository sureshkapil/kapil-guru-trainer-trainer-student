package com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model

import com.google.gson.annotations.SerializedName

data class QuestionPaperTitleRequest(
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("title") var title: String = "",
    @SerializedName("trainer_id") var trainerId: String = ""
)