package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class AddCouponsRequest(
    @SerializedName("course_id") var courseId: Int? = 0,
    @SerializedName("varid_upto") var varidUpto: String? = "",
    @SerializedName("coupon_code") var couponCode: String? = "",
    @SerializedName("student_id") var studentId: Int? = 0,
    @SerializedName("discount_percent") var discountPercent: Int? = 0,
    @SerializedName("created_by") var createdBy: Int? = 0
)