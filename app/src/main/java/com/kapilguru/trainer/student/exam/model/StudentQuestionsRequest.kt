package com.kapilguru.trainer.student.exam.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StudentQuestionsRequest(
    @SerializedName("batch_id") var batchId: Int?,
    @SerializedName("exam_id") var examId: Int?,
    @SerializedName("question_paper_id") var questionPaperId: Int?,
    @SerializedName("student_id") var studentId: Int?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(batchId)
        parcel.writeValue(examId)
        parcel.writeValue(questionPaperId)
        parcel.writeValue(studentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentQuestionsRequest> {
        override fun createFromParcel(parcel: Parcel): StudentQuestionsRequest {
            return StudentQuestionsRequest(parcel)
        }

        override fun newArray(size: Int): Array<StudentQuestionsRequest?> {
            return arrayOfNulls(size)
        }
    }
}