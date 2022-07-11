package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class MyCourseStudentsApi(
    @SerializedName("end_date") var endDate: String? = "",
    @SerializedName("course_id") var courseId: Int? = 0,
    @SerializedName("batch_id") var batchId: Int? = 0,
    @SerializedName("course_title") var courseTitle: String? = "",
    @SerializedName("student_id") var studentId: Int? = 0,
    @SerializedName("course_completion") var courseCompletion: Int? = 0,
    @SerializedName("student_code") var studentCode: String? = "",
    @SerializedName("batch_code") var batchCode: String? = "",
    @SerializedName("student_name") var studentName: String? = "",
    @SerializedName("sessions_count") var sessionsCount: Int? = 0,
    @SerializedName("percent_completion") var percentCompletion: Int? = 0,
    @SerializedName("refund_fee_req_id") var refundFeeReqId: Int? = 0,
    @SerializedName("attended_count") var attendedCount: Int? = 0,
    @SerializedName("conducted_count") var conductedCount: Int? = 0,
    @SerializedName("enrollment_date") var enrollmentDate: String? = "",
    @SerializedName("trainer_id") var trainerId: Int? = 0,
    @SerializedName("start_date") var startDate: String? = ""
)