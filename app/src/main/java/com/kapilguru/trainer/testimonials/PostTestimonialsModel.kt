package com.kapilguru.trainer.testimonials

import com.google.gson.annotations.SerializedName

data class PostTestimonialsModel(
    @SerializedName("tenant_id") var tenantId: Int? = 0,
    @SerializedName("comments") var comments: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("trainer_id") var trainerId: Int? = 0
)