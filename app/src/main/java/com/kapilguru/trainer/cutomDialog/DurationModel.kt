package com.kapilguru.trainer.cutomDialog


import android.os.Parcel
import android.os.Parcelable

class DurationModel(var key: String? = null, var value: Int? = null, var isSelected: Boolean) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeValue(key)
        parcel.writeValue(value)
        parcel.writeValue(isSelected)
    }

    companion object CREATOR : Parcelable.Creator<DurationModel> {
        override fun createFromParcel(parcel: Parcel): DurationModel {
            return DurationModel(parcel)
        }

        override fun newArray(size: Int): Array<DurationModel?> {
            return arrayOfNulls(size)
        }
    }
}

