package com.kapilguru.trainer.ui.courses.view_course

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.ui.courses.view_course.ContactTrainerResponse

data class ContactTrainerResponseAPi(
    @SerializedName("data")
    val contactTrainerResponse: ContactTrainerResponse,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)