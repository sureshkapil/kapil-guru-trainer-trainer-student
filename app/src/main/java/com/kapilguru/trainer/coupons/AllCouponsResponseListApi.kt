package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class AllCouponsResponseListApi(
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("course_title") val courseTitle: String? = "",
    @SerializedName("valid_upto") val validUpto: String = "",
    @SerializedName("coupon_code") var couponCode: String = "",
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("modified_by") val modifiedBy: String ="" ,
    @SerializedName("student_id") val studentId: Int = 0,
    @SerializedName("student_name") val studentName: String? = null,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("discount_percent") val discountPercent: Int = 0,
    @SerializedName("modified_date") val modifiedDate: String = "",
    @SerializedName("created_by") val createdBy: Int = 0
)