package com.kapilguru.trainer.ui.courses.tax

import android.os.Parcel
import android.os.Parcelable

class PriceModel() : Parcelable {
    var fee: String? = "0" // original price
    var discountAmount: String? = "0" // discount %
    var actualFee: String? = "0" // after discounted price
    var isInternetChargesAdded: Boolean? = false
    var isTaxChargesAdded: Boolean? = false
    var internetCharges: Double? = 0.0 // if 0

    constructor(parcel: Parcel) : this() {
        fee = parcel.readString()
        discountAmount = parcel.readString()
        actualFee = parcel.readString()
        isInternetChargesAdded = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        isTaxChargesAdded = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        internetCharges = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fee)
        parcel.writeString(discountAmount)
        parcel.writeString(actualFee)
        parcel.writeValue(isInternetChargesAdded)
        parcel.writeValue(isTaxChargesAdded)
        parcel.writeValue(internetCharges)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PriceModel> {
        override fun createFromParcel(parcel: Parcel): PriceModel {
            return PriceModel(parcel)
        }

        override fun newArray(size: Int): Array<PriceModel?> {
            return arrayOfNulls(size)
        }
    }

}