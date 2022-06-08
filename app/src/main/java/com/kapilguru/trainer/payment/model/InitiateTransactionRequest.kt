package com.kapilguru.trainer.payment.model

import android.net.wifi.hotspot2.pps.Credential
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

//Same class is  used for initTransactionRequest and TransactionStatusRequest
data class InitiateTransactionRequest(
    @SerializedName("orderId") var orderId :String? = "",
    @SerializedName("user_id") var userId :Int? = 0,
    @SerializedName("user_code") var userCode: String? = "",
    @SerializedName("amount") var amount :Double? = 0.0,
    @SerializedName("product_type") var productType :String? = "",
    @SerializedName("product_id") var productId :Int? = 0,
    @SerializedName("course_id") var courseId :Int? = null, //sent for Position and Best Trainer Subscription
    @SerializedName("course_position_num") var coursePositionNum :Int? = null, // sent for position Subscription
    @SerializedName("platformCharges") var platformCharges :Double? = 0.0,
    @SerializedName("cgst") var centralGST :Double? = 0.0,
    @SerializedName("sgst") var stateGST :Double? = 0.0,
    @SerializedName("grandTotal") var grandTotal :Double? = 0.0,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderId)
        parcel.writeValue(userId)
        parcel.writeString(userCode)
        parcel.writeValue(amount)
        parcel.writeString(productType)
        parcel.writeValue(productId)
        parcel.writeValue(courseId)
        parcel.writeValue(coursePositionNum)
        parcel.writeValue(platformCharges)
        parcel.writeValue(centralGST)
        parcel.writeValue(stateGST)
        parcel.writeValue(grandTotal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InitiateTransactionRequest> {
        override fun createFromParcel(parcel: Parcel): InitiateTransactionRequest {
            return InitiateTransactionRequest(parcel)
        }

        override fun newArray(size: Int): Array<InitiateTransactionRequest?> {
            return arrayOfNulls(size)
        }
    }
}
