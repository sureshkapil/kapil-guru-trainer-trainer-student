package com.kapilguru.trainer.ui.profile.bank.data

import com.google.gson.annotations.SerializedName

data class BankDetailsFetchResData(
    @SerializedName("id") var bankUpdateId : Int,
    @SerializedName("user_id") var userId : Int,
    @SerializedName("account_name") var accountName : String,
    @SerializedName("account_number") var accountNumber : String,
    @SerializedName("bank_name") var bankName : String,
    @SerializedName("branch_name") var branchName : String,
    @SerializedName("ifsc_code") var ifscCode : String,
    @SerializedName("is_active") var isActive : Int,
    @SerializedName("created_by") var createdBy : Int,
    @SerializedName("created_date") var createdDate : String,
    @SerializedName("modified_by") var modifiedBy : Int,
    @SerializedName("modified_date") var modifiedDate : String)
