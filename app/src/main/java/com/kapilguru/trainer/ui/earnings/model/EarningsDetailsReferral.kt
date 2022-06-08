package com.kapilguru.trainer.ui.earnings.model

import com.google.gson.annotations.SerializedName

data class EarningsDetailsReferral(
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("referral_id") var referralId: Int? = null,
    @SerializedName("referral_name") var referralName: String? = null,
    @SerializedName("referred_date") var referredDate: String? = null,
    @SerializedName("referred_to") var referredTo: String? = null,
    @SerializedName("subscription_date") var subscriptionDate: String? = null,
    @SerializedName("subscription_amount") var subscriptionAmount: Int? = null,
    @SerializedName("referral_amount") var referralAmount: Int? = null
) {
    var shouldShowChild = false

    fun getSubscriptionDateAfterNullCheck(): String {
        if (this.subscriptionDate.isNullOrBlank()) {
            return ""
        }
        return if (this.subscriptionDate?.toLowerCase() == "null") {
            ""
        } else {
            this.subscriptionDate!!
        }
    }

    fun getReferredDateAfterNullCheck(): String {
        return if (this.referredDate?.toLowerCase() == "null") {
            ""
        } else {
            this.referredDate!!
        }
    }
}