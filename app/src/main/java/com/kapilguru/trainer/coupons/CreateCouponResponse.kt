package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class CreateCouponResponse(
    @SerializedName("data") var createCouponResponseAPi: CreateCouponResponseAPi?, @SerializedName("message") var message: String? = "", @SerializedName("status") var status: Int? = 0
)