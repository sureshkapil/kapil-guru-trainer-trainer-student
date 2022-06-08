package com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.PreviousQuestionPapersListData
import org.json.JSONObject

data class QuestionPaper(
    @SerializedName("question_paper_id") var questionPaperId: Int = 0,
    @SerializedName("trainer_id") var trainerId: Int = 0,
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("title") var title: String? = "",
    @SerializedName("total_marks") var totalMarks: Int = 0,
    @SerializedName("pass_marks") var passMarks: Int = 0,
    @SerializedName("total_questions") var totalQuestions: Int = 0,
    @SerializedName("status") var status: String? = "",
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )

    fun parseQuestionPaper(questionPaperString: String): QuestionPaper {
        val jsonObject = JSONObject(questionPaperString)
        if (jsonObject.has("question_paper_id")) {
            this.questionPaperId = jsonObject.getInt("question_paper_id")
        }
        if (jsonObject.has("trainer_id")) {
            this.trainerId = jsonObject.getInt("trainer_id")
        }
        if (jsonObject.has("course_id")) {
            this.courseId = jsonObject.getInt("course_id")
        }
        if (jsonObject.has("title")) {
            this.title = jsonObject.getString("title")
        }
        if (jsonObject.has("total_marks")) {
            this.totalMarks = jsonObject.getInt("total_marks")
        }
        if (jsonObject.has("pass_marks")) {
            this.passMarks = jsonObject.getInt("pass_marks")
        }
        if (jsonObject.has("total_questions")) {
            this.totalQuestions = jsonObject.getInt("total_questions")
        }
        if (jsonObject.has("status")) {
            this.status = jsonObject.getString("status")
        }
        return this
    }

    fun createQuestionPaper(questionPaperId: Int, title: String): QuestionPaper {
        this.questionPaperId = questionPaperId
        this.title = title
        return this
    }

    fun getQuestionPaper(previousQuestionPaperData: PreviousQuestionPapersListData): QuestionPaper {
        this.questionPaperId = previousQuestionPaperData.id
        this.trainerId = previousQuestionPaperData.trainerId
        this.courseId = previousQuestionPaperData.courseId
        this.title = previousQuestionPaperData.title.toString()
        this.totalMarks = previousQuestionPaperData.totalMarks
        this.passMarks = previousQuestionPaperData.passMarks
        this.totalQuestions = previousQuestionPaperData.totalQuestions
        this.status = previousQuestionPaperData.status.toString()
        return this
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(questionPaperId)
        parcel.writeInt(trainerId)
        parcel.writeInt(courseId)
        parcel.writeString(title)
        parcel.writeInt(totalMarks)
        parcel.writeInt(passMarks)
        parcel.writeInt(totalQuestions)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuestionPaper> {
        override fun createFromParcel(parcel: Parcel): QuestionPaper {
            return QuestionPaper(parcel)
        }

        override fun newArray(size: Int): Array<QuestionPaper?> {
            return arrayOfNulls(size)
        }
    }
}