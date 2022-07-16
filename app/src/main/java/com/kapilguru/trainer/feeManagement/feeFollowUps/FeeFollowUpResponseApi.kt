package com.kapilguru.trainer.feeManagement.feeFollowUps

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FeeFollowUpResponseApi(
    @SerializedName("email_id") val emailId: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("due_date") val dueDate: String? = "",
    @SerializedName("course") val course: String? = "",
    @SerializedName("due_fee") val dueFee: Double? = 0.0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("paid_installments") val paidInstallments: Int = 0,
    @SerializedName("no_of_installments") val noOfInstallments: Int = 0,
    @SerializedName("contact_number") val contactNumber: String? = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(emailId)
        parcel.writeString(name)
        parcel.writeString(dueDate)
        parcel.writeString(course)
        parcel.writeValue(dueFee)
        parcel.writeInt(id)
        parcel.writeInt(paidInstallments)
        parcel.writeInt(noOfInstallments)
        parcel.writeString(contactNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeeFollowUpResponseApi> {
        override fun createFromParcel(parcel: Parcel): FeeFollowUpResponseApi {
            return FeeFollowUpResponseApi(parcel)
        }

        override fun newArray(size: Int): Array<FeeFollowUpResponseApi?> {
            return arrayOfNulls(size)
        }
    }
}