package com.kapilguru.trainer.feeManagement.paidRecords

import com.google.gson.annotations.SerializedName

data class StudentFeePaidResponseApi(
    @SerializedName("email_id") val emailId: String = "",
    @SerializedName("transaction_id") val transactionId: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("due_date") val dueDate: String = "",
    @SerializedName("course") val course: String = "",
    @SerializedName("paid_fee") val paidFee: Int = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("mode_of_payment") val modeOfPayment: String = "",
    @SerializedName("paid_installments") val paidInstallments: Int = 0,
    @SerializedName("no_of_installments") val noOfInstallments: Int = 0,
    @SerializedName("contact_number") val contactNumber: String = "",
    @SerializedName("paid_date") val paidDate: String = ""
)