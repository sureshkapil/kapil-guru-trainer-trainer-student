package com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

//data class of view and assign question
data class Question(
    @SerializedName("trainer_id") var trainerId: Int = 0,
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("row_id") var rowId: Int = 0,
    @SerializedName("question_id") var questionId: Int = 0,
    @SerializedName("question") var question: String? = "",
    @SerializedName("opt_1") var opt1: String? = "",
    @SerializedName("opt_2") var opt2: String? = "",
    @SerializedName("opt_3") var opt3: String? = "",
    @SerializedName("opt_4") var opt4: String? = "",
    @SerializedName("correct_opt") var correctOpt: String? = "",
    @SerializedName("marks") var marks: Int = 0,
    @SerializedName("status") var status: String? = "",
    var shouldShowOptions: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    )

    fun parseQuestions(questionsString: String?): ArrayList<Question> {
        val questions = ArrayList<Question>()
        if (TextUtils.isEmpty(questionsString)) return questions
        val jsonArray = JSONArray(questionsString)
        for (i in 0 until jsonArray.length()) {
            val question = parseQuestion(jsonArray.getString(i))
            questions.add(question)
        }
        return questions
    }

    private fun parseQuestion(questionString: String): Question {
        var question = Question()
        val jsonObject = JSONObject(questionString)
        question.trainerId = jsonObject.optInt("trainer_id", 0)
        question.courseId = jsonObject.optInt("course_id", 0)
        question.rowId = jsonObject.optInt("row_id")
        question.questionId = jsonObject.optInt("question_id")
        question.question = jsonObject.optString("question", "0")
        question.opt1 = jsonObject.optString("opt_1")
        question.opt2 = jsonObject.optString("opt_2")
        question.opt3 = jsonObject.optString("opt_3")
        question.opt4 = jsonObject.optString("opt_4")
        question.correctOpt = jsonObject.optString("correct_opt")
        question.marks = jsonObject.optInt("marks")
        question.status = jsonObject.optString("status")
        return question
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(trainerId)
        parcel.writeInt(courseId)
        parcel.writeInt(rowId)
        parcel.writeInt(questionId)
        parcel.writeString(question)
        parcel.writeString(opt1)
        parcel.writeString(opt2)
        parcel.writeString(opt3)
        parcel.writeString(opt4)
        parcel.writeString(correctOpt)
        parcel.writeInt(marks)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}