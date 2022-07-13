package com.kapilguru.trainer.addStudent.signedUpStudentList

import com.google.gson.annotations.SerializedName

data class SignedUpStudentsListResponseApi(
    @SerializedName("tenant_id") val tenantId: Int = 0,
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("student_id") val studentId: Int = 0,
    @SerializedName("student_code") val studentCode: String = "",
    @SerializedName("modified_date") val modifiedDate: String? = "",
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("student_name") val studentName: String = "",
    @SerializedName("student_contact") val studentContact: String = "",
    @SerializedName("modified_by") val modifiedBy: Int? = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("student_email") val studentEmail: String = "",
    @SerializedName("trainer_id") val trainerId: Int = 0
)