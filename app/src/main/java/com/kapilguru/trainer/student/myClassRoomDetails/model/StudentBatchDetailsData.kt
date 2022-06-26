package com.kapilguru.trainer.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName

data class StudentBatchDetailsData(
    @SerializedName("thu") val thu: Int = 0,
    @SerializedName("bcr_req_resp_status") val bcrReqRespStatus: String = "",
    @SerializedName("refund_fee_paid_date") val refundFeePaidDate: String? = null,
    @SerializedName("tue") val tue: Int = 0,
    @SerializedName("fee") val fee: Int = 0,
    @SerializedName("refund_fee_paid") val refundFeePaid: Int = 0,
    @SerializedName("students_count") val studentsCount: Int = 0,
    @SerializedName("batch_status") val batchStatus: String = "",
    @SerializedName("is_extended") val isExtended: Int = 0,
    @SerializedName("course_completion") val courseCompletion: Int = 0,
    @SerializedName("mon") val mon: Int = 0,
    @SerializedName("is_added_by_trainer") val isAddedByTrainer: Int = 0,
    @SerializedName("course_code") val courseCode: String = "",
    @SerializedName("percent_completion") val percentCompletion: Int = 0,
    @SerializedName("batch_type") val batchType: String = "",
    @SerializedName("trainer_name") val trainerName: String = "",
    @SerializedName("fri") val fri: Int = 0,
    @SerializedName("bcr_req_id") val bcrReqId: Int = 0,
    @SerializedName("conducted_count") val conductedCount: Int = 0,
    @SerializedName("student_rating_text") val studentRatingText: String = "",
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("course_title") val courseTitle: String = "",
    @SerializedName("batch_code") val batchCode: String = "",
    @SerializedName("refund_fee_request_date") val refundFeeRequestDate: String? = null,
    @SerializedName("bcr_req_resp_reason") val bcrReqRespReason: String? = null,
    @SerializedName("offer_price") val offerPrice: Int = 0,
    @SerializedName("fee_paid") val feePaid: Int = 0,
    @SerializedName("bcr_req_resp_date") val bcrReqRespDate: String? = null,
    @SerializedName("refund_fee_req_id") val refundFeeReqId: Int = 0,
    @SerializedName("end_date") val endDate: String = "",
    @SerializedName("batch_id") val batchId: Int = 0,
    @SerializedName("current_no_of_students") val currentNoOfStudents: Int = 0,
    @SerializedName("sun") val sun: Int = 0,
    @SerializedName("fee_paid_date") val feePaidDate: String = "",
    @SerializedName("max_no_of_students") val maxNoOfStudents: Int = 0,
    @SerializedName("refund_fee_request") val refundFeeRequest: Int = 0,
    @SerializedName("duration") val duration: Int = 0,
    @SerializedName("wed") val wed: Int = 0,
    @SerializedName("student_rating") val studentRating: Int = 0,
    @SerializedName("course_completion_date") val courseCompletionDate: String = "",
    @SerializedName("start_date") val startDate: String = "",
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("is_batch_filled") val isBatchFilled: Int = 0,
    @SerializedName("sat") val sat: Int = 0,
    @SerializedName("student_id") val studentId: Int = 0,
    @SerializedName("fee_paid_id") val feePaidId: String = "",
    @SerializedName("refunded_count") val refundedCount: Int = 0,
    @SerializedName("ratings_date") val ratingsDate: String = "",
    @SerializedName("is_refunded") val isRefunded: Int = 0,
    @SerializedName("trainer_code") val trainerCode: String = "",
    @SerializedName("duration_minutes") val durationMinutes: Int = 0,
    @SerializedName("sessions_count") val sessionsCount: Int = 0,
    @SerializedName("is_live") val isLive: Int = 0,
    @SerializedName("course_completion_count") val courseCompletionCount: Int = 0,
    @SerializedName("attended_count") val attendedCount: Int = 0)