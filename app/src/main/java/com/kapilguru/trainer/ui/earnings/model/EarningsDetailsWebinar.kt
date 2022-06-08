package com.kapilguru.trainer.ui.earnings.model

import com.google.gson.annotations.SerializedName

data class EarningsDetailsWebinar(
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("webinar_id") var webinarId: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("start_date") var startDate: String? = null,
    @SerializedName("attendee_id") var attendeeId: Int? = null,
    @SerializedName("attendee_name") var attendeeName: String? = null,
    @SerializedName("enrollment_date") var enrollmentDate: String? = null,
    @SerializedName("fee") var fee: Int? = null
) {
    var shouldShowChild = false
}