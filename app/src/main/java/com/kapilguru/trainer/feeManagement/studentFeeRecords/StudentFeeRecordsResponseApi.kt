package com.kapilguru.trainer.feeManagement.studentFeeRecords

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StudentFeeRecordsResponseApi(
    @SerializedName("tenant_id") val tenantId: Int = 0,
    @SerializedName("email_id") val emailId: String? = "",
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("due_date") val dueDate: String? = null,
    @SerializedName("paid_fee") val paidFee: Double? = 0.0,
    @SerializedName("due_fee") val dueFee: Double? = 0.0,
    @SerializedName("paid_installments") val paidInstallments: Int = 0,
    @SerializedName("no_of_installments") val noOfInstallments: Int = 0,
    @SerializedName("modified_date") val modifiedDate: String? = "",
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("contact_number") val contactNumber: String? = "",
    @SerializedName("total_fee") val totalFee: Double? = 0.0,
    @SerializedName("name") val name: String? = "",
    @SerializedName("modified_by") val modifiedBy: String? = "",
    @SerializedName("course") val course: String? = "",
    @SerializedName("date_of_joining") val dateOfJoining: String? = null,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String? = null,
    @SerializedName("trainer_id") val trainerId: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(tenantId)
        parcel.writeString(emailId)
        parcel.writeInt(isActive)
        parcel.writeString(dueDate)
        parcel.writeValue(paidFee)
        parcel.writeValue(dueFee)
        parcel.writeInt(paidInstallments)
        parcel.writeInt(noOfInstallments)
        parcel.writeString(modifiedDate)
        parcel.writeInt(createdBy)
        parcel.writeString(contactNumber)
        parcel.writeValue(totalFee)
        parcel.writeString(name)
        parcel.writeString(modifiedBy)
        parcel.writeString(course)
        parcel.writeString(dateOfJoining)
        parcel.writeInt(id)
        parcel.writeString(createdDate)
        parcel.writeInt(trainerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentFeeRecordsResponseApi> {
        override fun createFromParcel(parcel: Parcel): StudentFeeRecordsResponseApi {
            return StudentFeeRecordsResponseApi(parcel)
        }

        override fun newArray(size: Int): Array<StudentFeeRecordsResponseApi?> {
            return arrayOfNulls(size)
        }
    }
}