package com.kapilguru.trainer.referandearn.myReferrals.model

import com.google.gson.annotations.SerializedName

data class MyReferralResponse(
    @SerializedName("data") val myReferralsList: ArrayList<MyReferralResData>? = null,
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: Int? = 0)