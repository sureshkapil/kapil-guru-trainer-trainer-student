package com.kapilguru.trainer.ui.earnings.referralDetails

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.apiDateFormatWithoutT
import org.json.JSONObject

data class ReferralAmount(
    @SerializedName("invitee_email")
    var inviteeEmail: String? = null,
    @SerializedName("referral_name")
    var referralName: String? = null,
    @SerializedName("referred_to")
    var referredTo: String? = null,
    @SerializedName("user_id")
    var userId: Int? = null,
    @SerializedName("subscription_amount")
    var subscriptionAmount: Double? = null,
    @SerializedName("invitee_contact_number")
    var inviteeContactNumber: String? = null,
    @SerializedName("referred_date")
    var referredDate: String? = null,
    @SerializedName("referral_id")
    var referralId: Int? = null,
    @SerializedName("referral_amount")
    var referralAmount: Double? = null,
    @SerializedName("referral_type")
    var referralType: String? = null,
    @SerializedName("subscription_date")
    var subscriptionDate: String? = null
):Parcelable {
    init {
        referredDate = referredDate.apiDateFormatWithoutT()
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun getReferralAmountDetails(jsonstr: JSONObject): ReferralAmount {
        return ReferralAmount().apply {
            this.inviteeEmail = jsonstr.getString("invitee_email")
            this.referralName = jsonstr.getString("referral_name")
            this.referredTo = jsonstr.getString("referred_to")
            this.userId = jsonstr.getInt("user_id")
            this.subscriptionAmount = jsonstr.getDouble("subscription_amount")
            this.inviteeContactNumber = jsonstr.getString("invitee_contact_number")
            this.referredDate = jsonstr.getString("referred_date")
            this.referralId = jsonstr.getInt("referral_id")
            this.referralAmount = jsonstr.getDouble("referral_amount")
            this.referralType = jsonstr.getString("referral_type")
            this.subscriptionDate = jsonstr.getString("subscription_date")
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(inviteeEmail)
        parcel.writeString(referralName)
        parcel.writeString(referredTo)
        parcel.writeValue(userId)
        parcel.writeValue(subscriptionAmount)
        parcel.writeString(inviteeContactNumber)
        parcel.writeString(referredDate)
        parcel.writeValue(referralId)
        parcel.writeValue(referralAmount)
        parcel.writeString(referralType)
        parcel.writeString(subscriptionDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReferralAmount> {
        override fun createFromParcel(parcel: Parcel): ReferralAmount {
            return ReferralAmount(parcel)
        }

        override fun newArray(size: Int): Array<ReferralAmount?> {
            return arrayOfNulls(size)
        }
    }

}