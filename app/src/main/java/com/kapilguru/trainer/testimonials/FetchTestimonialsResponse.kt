package com.kapilguru.trainer.testimonials

import com.google.gson.annotations.SerializedName

data class FetchTestimonialsResponse(
    @SerializedName("data") var fetchTestimoialsResponseApi: List<FetchTestimonialsResponseApi>?,
    @SerializedName("message") var message: String? = "",
    @SerializedName("status") var status: Int? = 0
)