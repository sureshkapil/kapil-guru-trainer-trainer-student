package com.kapilguru.trainer.exams.previousQuestionPapersList.model

import com.google.gson.annotations.SerializedName

data class CopyFromQuesPaperRequest(
    @SerializedName("trainer_id") var trainerId: String = "",
    @SerializedName("course_id") var courseId: Int = -1,
    @SerializedName("exising_qp_id") var existingQuestionpaperId: String = "",
    @SerializedName("new_qp_title") var newQuestionPaperTitle: String = "",
)