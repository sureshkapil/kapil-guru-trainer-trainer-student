package com.kapilguru.trainer.feeManagement.studentFeeRecords

import com.google.gson.annotations.SerializedName

data class StudentFeeRecordsResponseApi(
    @SerializedName("tenant_id") val tenantId: Int = 0,
    @SerializedName("email_id") val emailId: String = "",
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("due_date") val dueDate: String = "",
    @SerializedName("paid_fee") val paidFee: Int = 0,
    @SerializedName("due_fee") val dueFee: Int = 0,
    @SerializedName("paid_installments") val paidInstallments: Int = 0,
    @SerializedName("no_of_installments") val noOfInstallments: Int = 0,
    @SerializedName("modified_date") val modifiedDate: String = "",
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("contact_number") val contactNumber: String = "",
    @SerializedName("total_fee") val totalFee: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("modified_by") val modifiedBy: String? = "",
    @SerializedName("course") val course: String = "",
    @SerializedName("date_of_joining") val dateOfJoining: String = "",
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("trainer_id") val trainerId: Int = 0
)