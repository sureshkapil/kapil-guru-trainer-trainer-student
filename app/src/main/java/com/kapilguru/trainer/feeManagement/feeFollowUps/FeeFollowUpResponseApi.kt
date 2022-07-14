package com.kapilguru.trainer.feeManagement.feeFollowUps

import com.google.gson.annotations.SerializedName

data class FeeFollowUpResponseApi(
    @SerializedName("email_id") val emailId: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("due_date") val dueDate: String = "",
    @SerializedName("course") val course: String = "",
    @SerializedName("due_fee") val dueFee: Double? = 0.0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("paid_installments") val paidInstallments: Int = 0,
    @SerializedName("no_of_installments") val noOfInstallments: Int = 0,
    @SerializedName("contact_number") val contactNumber: String = ""
)