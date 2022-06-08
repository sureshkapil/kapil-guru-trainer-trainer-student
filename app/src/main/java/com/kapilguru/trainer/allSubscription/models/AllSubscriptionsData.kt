package com.kapilguru.trainer.allSubscription.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AllSubscriptionsData(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("subscription_name") val subscriptionName: String? = null,
    @SerializedName("subscription_desc") val subscriptionDesc: String? = null,
    @SerializedName("subscription_type") val subscriptionType: String? = null,
    @SerializedName("subscription_sub_type") val subscriptionSubType: String? = null, //position,package,badge
    @SerializedName("subscription_duration") val subscriptionDuration: Int = 0,
    @SerializedName("subscription_fee") val subscriptionFee: Double = 0.0,
    @SerializedName("subscription_original_fee") val subscriptionOriginalFee: Double = 0.0,
    @SerializedName("subscription_category") val subscriptionCategory: String? = null, //null,individual,organisation,
    @SerializedName("created_by") val createdBy: String? = null,
    @SerializedName("created_date") val createdDate: String? = null, //2022-02-03T13:09:47.000Z
    @SerializedName("modified_by") val modifiedBy: String? = null,
    @SerializedName("modified_date") val modifiedDate: String? = null, //2022-02-08T19:45:52.000Z
    @SerializedName("is_active") val isActive: Int = 0,
    var shouldShow: Boolean = false

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(subscriptionName)
        parcel.writeString(subscriptionDesc)
        parcel.writeString(subscriptionType)
        parcel.writeString(subscriptionSubType)
        parcel.writeInt(subscriptionDuration)
        parcel.writeDouble(subscriptionFee)
        parcel.writeDouble(subscriptionOriginalFee)
        parcel.writeString(subscriptionCategory)
        parcel.writeString(createdBy)
        parcel.writeString(createdDate)
        parcel.writeString(modifiedBy)
        parcel.writeString(modifiedDate)
        parcel.writeInt(isActive)
        parcel.writeByte(if (shouldShow) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AllSubscriptionsData> {
        override fun createFromParcel(parcel: Parcel): AllSubscriptionsData {
            return AllSubscriptionsData(parcel)
        }

        override fun newArray(size: Int): Array<AllSubscriptionsData?> {
            return arrayOfNulls(size)
        }
    }
}