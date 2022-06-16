package com.kapilguru.trainer.testimonials

import com.google.gson.annotations.SerializedName

data class PostTestimonialsResponse(
    @SerializedName("data") val postTestimonialsResponseApi: PostTestimonialsResponseApi, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)