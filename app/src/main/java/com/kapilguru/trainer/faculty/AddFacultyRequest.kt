package com.kapilguru.trainer.faculty

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AddFacultyRequest(
    @SerializedName("email_id") var emailId: String? = "",
    @SerializedName("tenant_id") var tenantId: Int? = 0,
    @SerializedName("country_code") var countryCode: Int? = 91,
    @SerializedName("name") var name: String? = "",
    @SerializedName("contact_number") var contactNumber: String? = "",
    @SerializedName("trainer_id") var trainerId: Int? = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(emailId)
        parcel.writeValue(tenantId)
        parcel.writeValue(countryCode)
        parcel.writeString(name)
        parcel.writeString(contactNumber)
        parcel.writeValue(trainerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddFacultyRequest> {
        override fun createFromParcel(parcel: Parcel): AddFacultyRequest {
            return AddFacultyRequest(parcel)
        }

        override fun newArray(size: Int): Array<AddFacultyRequest?> {
            return arrayOfNulls(size)
        }
    }
}