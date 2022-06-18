package com.kapilguru.trainer.ui.courses.tax

import android.os.Parcel
import android.os.Parcelable

class PriceModel() : Parcelable {
    var price: Double? = 0.0
    var offerPrice: Double? = 0.0
    var isInternetChargesAdded: Boolean = false
    var isTaxChargesAdded: Boolean = false
    var finalPrice: Double? = 0.0

    constructor(parcel: Parcel) : this() {
        price = parcel.readValue(Double::class.java.classLoader) as? Double
        offerPrice = parcel.readValue(Double::class.java.classLoader) as? Double
        isInternetChargesAdded = parcel.readByte() != 0.toByte()
        isTaxChargesAdded = parcel.readByte() != 0.toByte()
        finalPrice = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(price)
        parcel.writeValue(offerPrice)
        parcel.writeByte(if (isInternetChargesAdded) 1 else 0)
        parcel.writeByte(if (isTaxChargesAdded) 1 else 0)
        parcel.writeValue(finalPrice)
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