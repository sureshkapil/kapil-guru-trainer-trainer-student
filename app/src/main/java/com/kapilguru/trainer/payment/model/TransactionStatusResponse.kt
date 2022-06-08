package com.kapilguru.trainer.payment.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TransactionStatusResponse(
    @SerializedName("head") var head: InitiateTransactionResDataHead? = null,
    @SerializedName("body") var body: TransactionStatusResDataBody? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(InitiateTransactionResDataHead::class.java.classLoader), parcel.readParcelable(TransactionStatusResDataBody::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(head, flags)
        parcel.writeParcelable(body, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionStatusResponse> {
        override fun createFromParcel(parcel: Parcel): TransactionStatusResponse {
            return TransactionStatusResponse(parcel)
        }

        override fun newArray(size: Int): Array<TransactionStatusResponse?> {
            return arrayOfNulls(size)
        }
    }
}
