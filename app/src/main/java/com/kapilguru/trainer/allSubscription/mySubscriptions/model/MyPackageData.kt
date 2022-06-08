package com.kapilguru.trainer.allSubscription.mySubscriptions.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class MyPackageData(
    var userId: String? = null,
    var subsId: String? = null,
    var subscriptionName: String? = null,
    var startDate: String? = null,
    var expiryDate: String? = null,
    var subsDuration: Int? = 0,
    var subsFee: Int? = 0
) :Parcelable{
    var shouldShowFooter = false

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
        shouldShowFooter = parcel.readByte() != 0.toByte()
    }

    fun getMyPackageObject(myPackage: JSONObject): MyPackageData {
        if (myPackage.has("user_id"))
            this.userId = myPackage.getString("user_id")
        if (myPackage.has("subs_id"))
            this.subsId = myPackage.getString("subs_id")
        if (myPackage.has("subscription_name"))
            this.subscriptionName = myPackage.getString("subscription_name")
        if (myPackage.has("start_date"))
            this.startDate = myPackage.getString("start_date")
        if (myPackage.has("expiry_date"))
            this.expiryDate = myPackage.getString("expiry_date")
        if (myPackage.has("subs_duration"))
            this.subsDuration = myPackage.getInt("subs_duration")
        if (myPackage.has("subs_fee"))
            this.subsFee = myPackage.getInt("subs_fee")
        return this
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(subsId)
        parcel.writeString(subscriptionName)
        parcel.writeString(startDate)
        parcel.writeString(expiryDate)
        parcel.writeValue(subsDuration)
        parcel.writeValue(subsFee)
        parcel.writeByte(if (shouldShowFooter) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyPackageData> {
        override fun createFromParcel(parcel: Parcel): MyPackageData {
            return MyPackageData(parcel)
        }

        override fun newArray(size: Int): Array<MyPackageData?> {
            return arrayOfNulls(size)
        }
    }
}
