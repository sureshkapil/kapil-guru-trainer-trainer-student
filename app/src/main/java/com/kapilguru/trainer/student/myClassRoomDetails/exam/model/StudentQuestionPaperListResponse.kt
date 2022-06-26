package com.kapilguru.trainer.student.myClassRoomDetails.exam.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListItemResData

data class StudentQuestionPaperListResponse(
    @SerializedName("data") var questionPaperList: ArrayList<StudentQuestionPaperListItemResData>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)