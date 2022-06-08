package com.kapilguru.trainer.studentExamBatchResult

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StudentExamPaperRequest(
    @SerializedName("batch_id")
    var batchId: Int = 0,
    @SerializedName("student_id")
    var studentId: Int = 0,
    @SerializedName("course_id")
    var courseId: Int = 0,
    @SerializedName("question_paper_id")
    var questionPaperId: Int = 0,
): Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.writeInt(studentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentExamPaperRequest> {
        override fun createFromParcel(parcel: Parcel): StudentExamPaperRequest {
            return StudentExamPaperRequest(parcel)
        }

        override fun newArray(size: Int): Array<StudentExamPaperRequest?> {
            return arrayOfNulls(size)
        }
    }
}