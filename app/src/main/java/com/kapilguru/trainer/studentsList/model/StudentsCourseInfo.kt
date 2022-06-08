package com.kapilguru.trainer.studentsList.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StudentsCourseInfo(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("course_title") val courseTitle: String?=null,
    @SerializedName("trainer_name") val trainerName: String?=null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(courseTitle)
        parcel.writeString(trainerName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentsCourseInfo> {
        override fun createFromParcel(parcel: Parcel): StudentsCourseInfo {
            return StudentsCourseInfo(parcel)
        }

        override fun newArray(size: Int): Array<StudentsCourseInfo?> {
            return arrayOfNulls(size)
        }
    }
}
