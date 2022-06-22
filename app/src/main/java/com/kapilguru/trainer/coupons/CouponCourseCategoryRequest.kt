package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class CouponCourseCategoryRequest(
    @SerializedName("is_recorded") var isRecorded: Int = 0, @SerializedName("trainer_id") var trainerId: Int = 0
)