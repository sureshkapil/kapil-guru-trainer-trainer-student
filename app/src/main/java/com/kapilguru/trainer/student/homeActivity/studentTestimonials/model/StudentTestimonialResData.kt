package com.kapilguru.trainer.student.homeActivity.studentTestimonials.model

import com.google.gson.annotations.SerializedName

data class StudentTestimonialResData(
    @SerializedName("tenant_id") val tenantId: Int = 0,
    @SerializedName("comments") val comments: String = "",
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("modified_by") val modifiedBy: Int? = null,
    @SerializedName("is_approved") val isApproved: Int = 0,
    @SerializedName("student_id") val studentId: Int = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("modified_date") val modifiedDate: String = "",
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("trainer_id") val trainerId: Int = 0)