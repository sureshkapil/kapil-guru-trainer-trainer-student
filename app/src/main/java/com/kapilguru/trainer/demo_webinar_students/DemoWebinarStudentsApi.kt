package com.kapilguru.trainer.demo_webinar_students

import com.google.gson.annotations.SerializedName

data class DemoWebinarStudentsApi(
    @SerializedName("price")
    var price: Int? = 0,
    @SerializedName("start_time")
    var startTime: String? = "",
    @SerializedName("webinar_date")
    var webinarDate: String? = "",
    @SerializedName("webinar_id")
    var webinarId: Int? = 0,
    @SerializedName("code")
    var code: String = "",
    @SerializedName("attendee_code")
    var attendeeCode: String = "",
    @SerializedName("attendee_id")
    var attendeeId: Int = 0,
    @SerializedName("attendee_name")
    var attendeeName: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("lecture_date")
    var lectureDate: String = "",
    @SerializedName("lecture_id")
    var lectureId: String = "",
    @SerializedName("time")
    var time: String = "",
)