package com.kapilguru.trainer.testimonials

import com.google.gson.annotations.SerializedName

data class PostTestimonialsResponseApi(
    @SerializedName("insertId") var insertId: Int? = 0
)