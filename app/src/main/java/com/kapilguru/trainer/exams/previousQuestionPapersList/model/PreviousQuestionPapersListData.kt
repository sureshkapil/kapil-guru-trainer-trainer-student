package com.kapilguru.trainer.exams.previousQuestionPapersList.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PreviousQuestionPapersListData(
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("title") val title: String? = "",
    @SerializedName("modified_date") val modifiedDate: String? = "",
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("total_marks") val totalMarks: Int = 0,
    @SerializedName("total_questions") val totalQuestions: Int = 0,
    @SerializedName("pass_marks") val passMarks: Int = 0,
    @SerializedName("modified_by") val modifiedBy: Int? = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String? = "",
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("status") val status: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    )

    fun isDraft(): Boolean {
        return status.equals("D")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeInt(isActive)
        parcel.writeString(title)
        parcel.writeString(modifiedDate)
        parcel.writeInt(createdBy)
        parcel.writeInt(totalMarks)
        parcel.writeInt(totalQuestions)
        parcel.writeInt(passMarks)
        parcel.writeValue(modifiedBy)
        parcel.writeInt(id)
        parcel.writeString(createdDate)
        parcel.writeInt(trainerId)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PreviousQuestionPapersListData> {
        override fun createFromParcel(parcel: Parcel): PreviousQuestionPapersListData {
            return PreviousQuestionPapersListData(parcel)
        }

        override fun newArray(size: Int): Array<PreviousQuestionPapersListData?> {
            return arrayOfNulls(size)
        }
    }
}