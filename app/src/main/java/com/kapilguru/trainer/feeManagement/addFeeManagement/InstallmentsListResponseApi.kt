package com.kapilguru.trainer.feeManagement.addFeeManagement

import com.google.gson.annotations.SerializedName

data class InstallmentsListResponseApi(
    @SerializedName("transaction_id") var transactionId: String? = null,
    @SerializedName("is_active") var isActive: Int? = null,
    @SerializedName("due_date") var dueDate: String? = "",
    @SerializedName("paid_fee") var paidFee: Double? = 0.0,
    @SerializedName("due_fee") var dueFee: Double? = null,
    @SerializedName("modified_date") var modifiedDate: String? = null,
    @SerializedName("created_by") var createdBy: Int? = null,
    @SerializedName("fee_id") var feeId: Int? = null,
    @SerializedName("modified_by") var modifiedBy: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("mode_of_payment") var modeOfPayment: String? = null,
    @SerializedName("created_date") var createdDate: String? = null,
    @SerializedName("paid_date") var paidDate: String? = null
)