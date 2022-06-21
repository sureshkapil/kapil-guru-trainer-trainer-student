package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class CouponLiveCoursesResponse(
    @SerializedName("data") val couponLiveCoursesResponseApi: List<CouponLiveCoursesResponseApi>?, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)