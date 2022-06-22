package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class AddCouponResponse(
    @SerializedName("data") val addCouponResponseApi: AddCouponResponseApi?, @SerializedName("message") val message: String? = "", @SerializedName("status") val status: Int? = 0
)