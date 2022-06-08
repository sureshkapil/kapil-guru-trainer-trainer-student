package com.kapilguru.trainer.exams.createQuestion.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.Question

data class UpdateQuestionRequest(
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("correct_opt") var correctOpt: String = "",
    @SerializedName("question") var question: String = "",
    @SerializedName("opt_1") var opt1: String = "",
    @SerializedName("opt_2") var opt2: String = "",
    @SerializedName("opt_3") var opt3: String = "",
    @SerializedName("opt_4") var opt4: String = "",
    @SerializedName("marks") var marks: String = "",
    @SerializedName("trainer_id") var trainerId: String = ""
){
    fun getObject(addQuestionReq : AddQuestionRequest) : UpdateQuestionRequest{
        this.courseId = addQuestionReq.courseId
        this.correctOpt = addQuestionReq.correctOpt
        this.question = addQuestionReq.question
        this.opt1 = addQuestionReq.opt1
        this.opt2 = addQuestionReq.opt2
        this.opt3 = addQuestionReq.opt3
        this.opt4 = addQuestionReq.opt4
        this.marks = addQuestionReq.marks
        this.trainerId = addQuestionReq.trainerId
        return this
    }

}

