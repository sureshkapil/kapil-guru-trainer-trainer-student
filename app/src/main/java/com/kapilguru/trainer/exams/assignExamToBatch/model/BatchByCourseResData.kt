package com.kapilguru.trainer.exams.assignExamToBatch.model

import com.google.gson.annotations.SerializedName

data class BatchByCourseResData(
    @SerializedName("end_date") val endDate: String = "",
    @SerializedName("thu") val thu: Int = 0,
    @SerializedName("batch_id") val batchId: Int = 0,
    @SerializedName("current_no_of_students") val currentNoOfStudents: Int = 0,
    @SerializedName("tue") val tue: Int = 0,
    @SerializedName("fee") val fee: Int = 0,
    @SerializedName("students_count") val studentsCount: Int = 0,
    @SerializedName("mon") val mon: Int = 0,
    @SerializedName("sun") val sun: Int = 0,
    @SerializedName("max_no_of_students") val maxNoOfStudents: Int = 0,
    @SerializedName("duration") val duration: Int = 0,
    @SerializedName("course_code") val courseCode: String = "",
    @SerializedName("wed") val wed: Int = 0,
    @SerializedName("percent_completion") val percentCompletion: Int = -1,
    @SerializedName("batch_type") val batchType: String = "",
    @SerializedName("trainer_name") val trainerName: String = "",
    @SerializedName("fri") val fri: Int = 0,
    @SerializedName("conducted_count") val conductedCount: Int = 0,
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("start_date") val startDate: String = "",
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("is_batch_filled") val isBatchFilled: Int = 0,
    @SerializedName("course_title") val courseTitle: String = "",
    @SerializedName("sat") val sat: Int = 0,
    @SerializedName("batch_code") val batchCode: String = "",
    @SerializedName("offer_price") val offerPrice: Int = 0,
    @SerializedName("refunded_count") val refundedCount: Int = 0,
    @SerializedName("trainer_code") val trainerCode: String = "",
    @SerializedName("duration_minutes") val durationMinutes: Int = 0,
    @SerializedName("sessions_count") val sessionsCount: Int = 0,
    @SerializedName("course_completion_count") val courseCompletionCount: Int = 0,
    var isSelected : Boolean = false
    )