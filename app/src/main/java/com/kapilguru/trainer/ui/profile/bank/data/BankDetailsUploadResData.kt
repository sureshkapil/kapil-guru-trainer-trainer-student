package com.kapilguru.trainer.ui.profile.bank.data

import com.google.gson.annotations.SerializedName

data class BankDetailsUploadResData(
    @SerializedName("fieldCount") var fieldCount: Int,
    @SerializedName("affectedRows") var affectedRows: Int,
    @SerializedName("insertId") var insertId: Int,
    @SerializedName("serverStatus") var serverStatus: Int,
    @SerializedName("warningCount") var warningCount: Int,
    @SerializedName("message") var message: String,
    @SerializedName("protocol41") var protocol41: Boolean,
    @SerializedName("changedRows") var changedRows: Int
)

