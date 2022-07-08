package com.kapilguru.trainer.ui.courses.courses_list.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CourseResponse(
    @SerializedName("batches_count") var batchesCount: Int? = -1,
    @SerializedName("course_id") val courseId: Int? = -1,
    @SerializedName("course_code") var courseCode: String? ="",
    @SerializedName("course_title") val courseTitle: String? = "",
    @SerializedName("students_count") val studentsCount: Int? = -1,
    @SerializedName("trainer_id") val trainerId: Int? = -1,
    @SerializedName("trainer_name") val trainerName: String? = "",
    @SerializedName("is_verified") var isVerified: Int? = 0,
    @SerializedName("is_rejected") var isRejected: Int? = 0,
    @SerializedName("is_submitted") var isSubmitted: Int? = 0,
    var isSelected :Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
    )

    fun convertedBatchCountToString(): String {
        if (batchesCount == null) {
            batchesCount = 0
        }
        return batchesCount.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(batchesCount)
        parcel.writeValue(courseId)
        parcel.writeString(courseCode)
        parcel.writeString(courseTitle)
        parcel.writeValue(studentsCount)
        parcel.writeValue(trainerId)
        parcel.writeString(trainerName)
        parcel.writeValue(isVerified)
        parcel.writeValue(isRejected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CourseResponse> {
        override fun createFromParcel(parcel: Parcel): CourseResponse {
            return CourseResponse(parcel)
        }

        override fun newArray(size: Int): Array<CourseResponse?> {
            return arrayOfNulls(size)
        }
    }

}