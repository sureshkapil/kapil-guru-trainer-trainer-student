package com.kapilguru.trainer.student.homeActivity.studentTestimonials.model

import com.google.gson.annotations.SerializedName

data class StudentTestimonialResponse(
    @SerializedName("data") val studentTestimonialList: ArrayList<StudentTestimonialResData>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)