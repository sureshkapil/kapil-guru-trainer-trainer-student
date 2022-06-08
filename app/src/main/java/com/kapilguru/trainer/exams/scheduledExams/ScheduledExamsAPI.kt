package com.kapilguru.trainer.exams.scheduledExams

import com.google.gson.annotations.SerializedName

data class ScheduledExamsAPI(
    @SerializedName("data")
    val scheduleExamsResponse: List<ScheduleExamsResponse>?,
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("status")
    val status: Int? = 0
)