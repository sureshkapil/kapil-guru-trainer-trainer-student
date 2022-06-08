package com.kapilguru.trainer.ui.courses.view_course

import com.google.gson.annotations.SerializedName

data class WriteReviewRequest(

    @SerializedName("student_rating")
    var studentRating: Float,
    @SerializedName("student_rating_text")
    var studentRatingText: String,
    @SerializedName("s_id")
    var sId: String,
    @SerializedName("batch_id")
    var batchId: Int
)
