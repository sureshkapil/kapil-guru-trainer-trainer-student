package com.kapilguru.trainer.student.myClassRoomDetails.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ReviewStudentRequest(
    @SerializedName("student_rating") var studentRating: Float,
    @SerializedName("student_rating_text") var studentRatingText: String,
    @SerializedName("batch_id") var batchId: Int?,
    @SerializedName("s_id") var studentId: String
)
