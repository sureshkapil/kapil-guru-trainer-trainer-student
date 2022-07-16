package com.kapilguru.trainer.feeManagement.addFeeManagement

import com.google.gson.annotations.SerializedName

data class AddFeeManagementRequest(
    @SerializedName("email_id") var emailId: String? = "",
    @SerializedName("tenant_id") var tenantId: Int? = 0,
    @SerializedName("total_fee") var totalFee: String? = "",
    @SerializedName("name") var name: String? = "",
//    @SerializedName("due_date") var dueDate: String? = "",
    @SerializedName("date_of_joining") var dateOfJoining: String? = "",
    @SerializedName("course") var course: String? = "",
    @SerializedName("paid_fee") var paidFee: String? = "",
    @SerializedName("due_fee") var dueFee: Double? = 0.0,
    @SerializedName("no_of_installments") var noOfInstallments: String? = "",
    @SerializedName("contact_number") var contactNumber: String? = "",
    @SerializedName("trainer_id") var trainerId: Int? = 0
)