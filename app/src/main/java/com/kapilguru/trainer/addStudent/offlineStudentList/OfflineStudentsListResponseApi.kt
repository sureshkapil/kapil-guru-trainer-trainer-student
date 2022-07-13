package com.kapilguru.trainer.addStudent.offlineStudentList

import com.google.gson.annotations.SerializedName

data class OfflineStudentsListResponseApi(
    @SerializedName("tenant_id") val tenantId: Int = 0,
    @SerializedName("email_id") val emailId: String = "",
    @SerializedName("end_date") val endDate: String? = null,
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("fee") val fee: Int = 0,
    @SerializedName("modified_date") val modifiedDate: String? = null,
    @SerializedName("batch_date") val batchDate: String = "",
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("contact_number") val contactNumber: String = "",
    @SerializedName("days_attended") val daysAttended: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("modified_by") val modifiedBy: Int = 0,
    @SerializedName("course") val course: String = "",
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("days_conducted") val daysConducted: Int = 0,
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("status") val status: String = ""
)