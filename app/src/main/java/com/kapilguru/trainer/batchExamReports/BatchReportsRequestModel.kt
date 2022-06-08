package com.kapilguru.trainer.batchExamReports

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BatchReportsRequestModel(
    @SerializedName("course_id")
    var courseId: Int = 0,
    @SerializedName("question_paper_id")
    var questionPaperId: Int = 0,
    @SerializedName("batch_id")
    var batchId: Int = 0,
    @SerializedName("trainer_id")
    var trainerId: Int = 0,
    @SerializedName("exam_id")
    var examId: Int = 0,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeInt(questionPaperId)
        parcel.writeInt(batchId)
        parcel.writeInt(trainerId)
        parcel.writeInt(examId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BatchReportsRequestModel> {
        override fun createFromParcel(parcel: Parcel): BatchReportsRequestModel {
            return BatchReportsRequestModel(parcel)
        }

        override fun newArray(size: Int): Array<BatchReportsRequestModel?> {
            return arrayOfNulls(size)
        }
    }
}