package com.kapilguru.trainer.ui.refund.model

import android.util.Log
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Keep
data class RefundData(
    @SerializedName("batch_id") var batchId: Int?= null,
    @SerializedName("course_completion")var courseCompletion: Int?= null,
    @SerializedName("course_id")var courseId: Int?= null,
    @SerializedName("course_title")var courseTitle: String?= null,
    @SerializedName("enrollment_date")var enrollmentDate: String?= null,
    @SerializedName("fee_paid")var feePaid: Int?= null,
    @SerializedName("fee_paid_date")var feePaidDate: String?= null,
    @SerializedName("is_refunded")var isRefunded: Int?= null,
    @SerializedName("percent_completion")var percentCompletion: Int?= null,
    @SerializedName("refund_fee_paid")var refundFeePaid: Int?= null,
    @SerializedName("refund_fee_paid_date")var refundFeePaidDate: String?= null,
    @SerializedName("refund_fee_request")var refundFeeRequest: Int?= null,
    @SerializedName("refund_fee_request_date")var refundFeeRequestDate: String?= null,
    @SerializedName("start_date")var startDate: String?= null,
    @SerializedName("start_time")var startTime: String?= null,
    @SerializedName("student_id")var studentId: Int?= null,
    @SerializedName("student_name")var studentName: String?= null,
    @SerializedName("student_rating")var studentRating: Int?= null,
    @SerializedName("trainer_id")var trainerId: Int?= null,
    @SerializedName("trainer_name")var trainerName: String?= null
) : Serializable