package com.kapilguru.trainer.ui.earnings.history.model

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EarningsHistoryResponse(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("user_id")
    val userId: Int? = 0,
    @SerializedName("user_name")
    val userName: String? = "",
    @SerializedName("webinars_amount")
    val webinarsAmount: Double? = 0.0,
    @SerializedName("referrals_amount")
    val referralsAmount: Double? = 0.0,
    @SerializedName("courses_amount")
    val coursesAmount: Double? = 0.0,
    @SerializedName("deducted_amount")
    val deductedAmount: Int? = 0,
    @SerializedName("total_amount")
    val totalAmount: Int? = 0,
    @SerializedName("payable_amount")
    val payableAmount: Double? = 0.0,
    @SerializedName("created_date")
    var createdDate: String? = "",
    @SerializedName("created_by")
    val createdBy: Int? = 0,
    @SerializedName("is_paid")
    val isPaid: Int? = 0,
    @SerializedName("is_processed")
    val isProcessed: Int? = 0,
    @SerializedName("paid_money_id")
    val paidMoneyId: Int? = 0,
    @SerializedName("remarks")
    val remarks: String? = null,
    @SerializedName("status")
    val status: String = "",
    @Expose(serialize = false , deserialize = false)
    val shouldShow: MutableLiveData<Boolean> = MutableLiveData(false)
) {


}