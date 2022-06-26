package com.kapilguru.trainer.student.myClassRoomDetails.exam.model

import com.google.gson.annotations.SerializedName

data class StudentQuestionPaperListRequest(
    @SerializedName("batch_id") val batchId: Int = 0,
    @SerializedName("student_id") val studentId: String = "")