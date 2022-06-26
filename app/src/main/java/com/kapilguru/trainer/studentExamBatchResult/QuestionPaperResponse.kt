package com.kapilguru.trainer.studentExamBatchResult

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuestionPaperResponse(
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("correct_opt") var correctOpt: String? = null,
    @SerializedName("question") var question: String = "",
    @SerializedName("opt_1") var opt1: String = "",
    @SerializedName("opt_2") var opt2: String = "",
    @SerializedName("opt_4") var opt4: String = "",
    @SerializedName("marks") var marks: Int = 0,
    @SerializedName("row_id") var rowId: Int = 0,
    @SerializedName("opt_3") var Opt3: String = "",
    @SerializedName("question_id") var questionId: Int = 0,
    @SerializedName("selected_opt") var selectedOpt: String? = null,
    @SerializedName("trainer_id") var trainerId: Int = 0,
    @Expose(serialize = false,deserialize = false) var isSelected: Boolean = false
)