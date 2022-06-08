package com.kapilguru.trainer.ui.earnings.model

import com.google.gson.annotations.SerializedName

data class EarningsDetailsPayment(
    @SerializedName("paid_money_id") var paidMoneyId: Int? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("request_money_id") var requestMoneyId: Int? = null,
    @SerializedName("paid_amount") var paidAmount: Int? = null,
    @SerializedName("paid_date") var paidDate: String? = null,
    @SerializedName("remarks") var remarks: String? = null,
    @SerializedName("account_name") var accountName: String? = null,
    @SerializedName("account_number") var accountNumber: String? = null,
    @SerializedName("bank_name") var bankName: String? = null,
    @SerializedName("branch_name") var branchName: String? = null,
    @SerializedName("ifsc_code") var ifscCode: String? = null,
    @SerializedName("transaction_summary") var transactionSummary: String? = null
){
    var shouldShowChild = false
}