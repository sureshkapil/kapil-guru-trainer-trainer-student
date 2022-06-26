package com.kapilguru.trainer.testimonials

import com.google.gson.annotations.SerializedName

data class TestimonialApproveRequest(
    @SerializedName("is_approved") val isApproved: Int? = 1
)