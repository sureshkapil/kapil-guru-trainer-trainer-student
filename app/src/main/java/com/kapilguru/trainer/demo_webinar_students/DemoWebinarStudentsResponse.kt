package com.kapilguru.trainer.demo_webinar_students

import com.google.gson.annotations.SerializedName

data class DemoWebinarStudentsResponse(
    @SerializedName("data")
    val data: List<DemoWebinarStudentsApi>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)