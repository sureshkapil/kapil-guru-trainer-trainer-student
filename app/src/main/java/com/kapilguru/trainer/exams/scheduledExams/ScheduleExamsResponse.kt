package com.kapilguru.trainer.exams.scheduledExams

import com.google.gson.annotations.SerializedName

data class ScheduleExamsResponse(
    @SerializedName("course_id")
    val courseId: Int = 0,
    @SerializedName("batch_id")
    val batchId: Int = 0,
    @SerializedName("batch_code")
    val batchCode: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("duration")
    val duration: Int = 0,
    @SerializedName("question_paper_id")
    val questionPaperId: Int = 0,
    @SerializedName("total_marks")
    val totalMarks: Int = 0,
    @SerializedName("total_questions")
    val totalQuestions: Int = 0,
    @SerializedName("batch_time")
    val batchTime: String = "",
    @SerializedName("examdate")
    val examdate: String = "",
    @SerializedName("trainer_name")
    val trainerName: String = "",
    @SerializedName("course_title")
    val courseTitle: String = "",
    @SerializedName("exam_id")
    val examId: Int = 0,
    @SerializedName("trainer_id")
    val trainerId: Int = 0
)