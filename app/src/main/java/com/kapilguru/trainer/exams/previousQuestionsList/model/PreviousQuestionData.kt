package com.kapilguru.trainer.exams.previousQuestionsList.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

//used in copy questions
data class PreviousQuestionData(
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("correct_opt") val correctOpt: String? = "",
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("question") val question: String? = "",
    @SerializedName("marks") val marks: Int = 0,
    @SerializedName("modified_date") val modifiedDate: String? = "",
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("opt_1") val opt1: String? = "",
    @SerializedName("opt_2") val opt2: String? = "",
    @SerializedName("opt_3") val opt3: String? = "",
    @SerializedName("opt_4") val opt4: String? = "",
    @SerializedName("modified_by") val modifiedBy: Int = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String? = "",
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("status") val status: String? = "",
    var isSelected: Boolean = false,
    var shouldShowOptions: Boolean = false,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeString(correctOpt)
        parcel.writeInt(isActive)
        parcel.writeString(question)
        parcel.writeInt(marks)
        parcel.writeString(modifiedDate)
        parcel.writeInt(createdBy)
        parcel.writeString(opt1)
        parcel.writeString(opt2)
        parcel.writeString(opt3)
        parcel.writeString(opt4)
        parcel.writeInt(modifiedBy)
        parcel.writeInt(id)
        parcel.writeString(createdDate)
        parcel.writeInt(trainerId)
        parcel.writeString(status)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeByte(if (shouldShowOptions) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PreviousQuestionData> {
        override fun createFromParcel(parcel: Parcel): PreviousQuestionData {
            return PreviousQuestionData(parcel)
        }

        override fun newArray(size: Int): Array<PreviousQuestionData?> {
            return arrayOfNulls(size)
        }
    }
}