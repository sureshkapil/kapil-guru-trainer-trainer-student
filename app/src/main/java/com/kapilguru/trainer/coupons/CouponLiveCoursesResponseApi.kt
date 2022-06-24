package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class CouponLiveCoursesResponseApi(
    @SerializedName("course_id") val courseId: Int? = 0,
    @SerializedName("coupon_code") val couponCode: String? = "",
    @SerializedName("is_active") val isActive: Int? = 0,
    @SerializedName("course_title") val courseTitle: String? = "",
    @SerializedName("student_id") val studentId: Int? = 0,
    @SerializedName("modified_date") val modifiedDate: String? = null,
    @SerializedName("created_by") val createdBy: Int? = 0,
    @SerializedName("student_name") val studentName: String? = null,
    @SerializedName("valid_upto") val validUpto: String? = "",
    @SerializedName("modified_by") val modifiedBy: String? = null,
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("created_date") val createdDate: String? = "",
    @SerializedName("discount_percent") val discountPercent: Int? = 0
)