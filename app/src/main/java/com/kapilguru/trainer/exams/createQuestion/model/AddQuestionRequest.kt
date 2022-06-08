package com.kapilguru.trainer.exams.createQuestion.model

import com.google.gson.annotations.SerializedName

data class AddQuestionRequest(
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("correct_opt") var correctOpt: String = "",
    @SerializedName("question_paper_id") var questionPaperId: Int = 0,
    @SerializedName("question") var question: String = "",
    @SerializedName("opt_1") var opt1: String = "",
    @SerializedName("opt_2") var opt2: String = "",
    @SerializedName("opt_3") var opt3: String = "",
    @SerializedName("opt_4") var opt4: String = "",
    @SerializedName("marks") var marks: String = "",
    @SerializedName("trainer_id") var trainerId: String = "")