package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class AllCouponsResponseList(
    @SerializedName("data") var allCouponsResponseListApi: List<AllCouponsResponseListApi>?,
    @SerializedName("message") var message: String? = "",
    @SerializedName("status") var status: Int? = 0
)