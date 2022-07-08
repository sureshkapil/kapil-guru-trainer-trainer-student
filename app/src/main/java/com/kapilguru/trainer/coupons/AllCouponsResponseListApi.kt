package com.kapilguru.trainer.coupons

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AllCouponsResponseListApi(
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("course_title") val courseTitle: String? = "",
    @SerializedName("valid_upto") val validUpto: String? = "",
    @SerializedName("coupon_code") var couponCode: String? = "",
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("modified_by") val modifiedBy: String? ="" ,
    @SerializedName("student_id") val studentId: Int = 0,
    @SerializedName("student_name") val studentName: String? = null,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String? = "",
    @SerializedName("discount_percent") val discountPercent: Int = 0,
    @SerializedName("modified_date") val modifiedDate: String? = "",
    @SerializedName("created_by") val createdBy: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeString(courseTitle)
        parcel.writeString(validUpto)
        parcel.writeString(couponCode)
        parcel.writeInt(isActive)
        parcel.writeString(modifiedBy)
        parcel.writeInt(studentId)
        parcel.writeString(studentName)
        parcel.writeInt(id)
        parcel.writeString(createdDate)
        parcel.writeInt(discountPercent)
        parcel.writeString(modifiedDate)
        parcel.writeInt(createdBy)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AllCouponsResponseListApi> {
        override fun createFromParcel(parcel: Parcel): AllCouponsResponseListApi {
            return AllCouponsResponseListApi(parcel)
        }

        override fun newArray(size: Int): Array<AllCouponsResponseListApi?> {
            return arrayOfNulls(size)
        }
    }
}