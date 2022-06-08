package com.kapilguru.trainer.ui.earnings.model

import com.google.gson.annotations.SerializedName

data class EarningsListApiData(
    @SerializedName("courses_amount_available") var coursesAmountAvailable: Double,
    @SerializedName("courses_amount_expected")  var coursesAmountExpected: Double,
    @SerializedName("courses_amount_received")  var coursesAmountReceived: Int,
    @SerializedName("courses_amount_requested")  var coursesAmountRequested: Int,
    @SerializedName("referral_amount_available")  var referralAmountAvailable: Double,
    @SerializedName("referral_amount_expected")  var referralAmountExpected: Double,
    @SerializedName("referral_amount_received") var referralAmountReceived: Int,
    @SerializedName("referral_amount_requested")  var referralAmountRequested: Int,
    @SerializedName("user_id")  var userId: Int,
    @SerializedName("webinar_amount_available")   var webinarAmountAvailable: Double,
    @SerializedName("webinar_amount_expected")  var webinarAmountExpected: Double,
    @SerializedName("webinar_amount_received") var webinarAmountReceived: Int,
    @SerializedName("webinar_amount_requested")  var webinarAmountRequested: Int
)