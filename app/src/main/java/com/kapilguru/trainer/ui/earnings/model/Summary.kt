package com.kapilguru.trainer.ui.earnings.model

import com.google.gson.annotations.SerializedName

data class Summary(
    @SerializedName("user_id") var userId: String,
    @SerializedName("courses_amount_expected") var coursesAmountExpected: Int,
    @SerializedName("referral_amount_expected") var referralAmountExpected: Int,
    @SerializedName("webinar_amount_expected") var webinarAmountExpected: Int,
    @SerializedName("courses") var courses: List<String>
    )