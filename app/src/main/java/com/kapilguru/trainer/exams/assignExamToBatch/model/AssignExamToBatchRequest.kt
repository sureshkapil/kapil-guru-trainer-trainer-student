package com.kapilguru.trainer.exams.assignExamToBatch.model

import com.google.gson.annotations.SerializedName

data class AssignExamToBatchRequest(
    @SerializedName("duration") var duration: String = "",
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("question_paper_id") var questionPaperId: Int = 0,
    @SerializedName("batch_id") var batchId: Int = 0,
    @SerializedName("examdate") var examdate: String = "",
    @SerializedName("trainer_id") var trainerId: String = "")