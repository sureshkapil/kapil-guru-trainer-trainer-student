package com.kapilguru.trainer.ui.courses.batchesList.batchStudents.models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class BatchStudentListResponse(
    @SerializedName("batch_id") val batchId: Int,
    @SerializedName("course_completion") val courseCompletion: Int,
    @SerializedName("course_id") val courseId: Int,
    @SerializedName("course_title") val courseTitle: String,
    @SerializedName("percent_completion") val percentCompletion: Int,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("student_id") val studentId: Int,
    @SerializedName("student_name") val studentName: String,
    @SerializedName("student_rating") val studentRating: Int,
    @SerializedName("trainer_id") val trainerId: Int
)