package com.kapilguru.trainer.feeManagement.paidRecords

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StudentFeePaidResponseApi(
    @SerializedName("email_id") val emailId: String? = "",
    @SerializedName("transaction_id") val transactionId: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("due_date") val dueDate: String? = "",
    @SerializedName("course") val course: String? = "",
    @SerializedName("paid_fee") val paidFee: Double = 0.0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("mode_of_payment") val modeOfPayment: String? = "",
    @SerializedName("paid_installments") val paidInstallments: Int = 0,
    @SerializedName("no_of_installments") val noOfInstallments: Int = 0,
    @SerializedName("contact_number") val contactNumber: String? = "",
    @SerializedName("paid_date") val paidDate: String? = ""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(emailId)
        parcel.writeString(transactionId)
        parcel.writeString(name)
        parcel.writeString(dueDate)
        parcel.writeString(course)
        parcel.writeDouble(paidFee)
        parcel.writeInt(id)
        parcel.writeString(modeOfPayment)
        parcel.writeInt(paidInstallments)
        parcel.writeInt(noOfInstallments)
        parcel.writeString(contactNumber)
        parcel.writeString(paidDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentFeePaidResponseApi> {
        override fun createFromParcel(parcel: Parcel): StudentFeePaidResponseApi {
            return StudentFeePaidResponseApi(parcel)
        }

        override fun newArray(size: Int): Array<StudentFeePaidResponseApi?> {
            return arrayOfNulls(size)
        }
    }
}