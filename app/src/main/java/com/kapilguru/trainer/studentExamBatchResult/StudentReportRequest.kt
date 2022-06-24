package com.kapilguru.trainer.studentExamBatchResult

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StudentReportRequest(
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("question_paper_id") var questionPaperId: Int = 0,
    @SerializedName("batch_id") var batchId: Int = 0,
    @SerializedName("student_id") var studentId: Int = 0,
    @SerializedName("trainer_id") var trainerId: Int = 0,
    @SerializedName("exam_id") var examId: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeInt(questionPaperId)
        parcel.writeInt(batchId)
        parcel.writeInt(studentId)
        parcel.writeInt(trainerId)
        parcel.writeInt(examId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentReportRequest> {
        override fun createFromParcel(parcel: Parcel): StudentReportRequest {
            return StudentReportRequest(parcel)
        }

        override fun newArray(size: Int): Array<StudentReportRequest?> {
            return arrayOfNulls(size)
        }
    }
}