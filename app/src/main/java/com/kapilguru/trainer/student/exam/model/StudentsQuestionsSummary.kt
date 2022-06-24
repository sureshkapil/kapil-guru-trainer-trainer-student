package com.kapilguru.trainer.student.exam.model


import com.google.gson.annotations.SerializedName

data class StudentsQuestionsSummary(
    @SerializedName("batch_id") var batchId: String?,
    @SerializedName("exam_id") var examId: String?,
    @SerializedName("question_paper_id") var questionPaperId: String?,
    @SerializedName("student_code") var studentCode: String?,
    @SerializedName("student_id") var studentId: String?,
    @SerializedName("student_name") var studentName: String?
)