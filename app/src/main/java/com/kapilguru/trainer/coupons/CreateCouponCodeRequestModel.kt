package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class CreateCouponCodeRequestModel(
    @SerializedName("course_id") var courseId: Int? = 0,
    @SerializedName("valid_upto") var validUpto: String? = "",
    @SerializedName("coupon_code") var couponCode: String? = "",
    @SerializedName("student_id") var studentId: Int? = null,
    @SerializedName("discount_percent") var discountPercent: Int? = 0,
    @SerializedName("created_by") var createdBy: String? = ""
)