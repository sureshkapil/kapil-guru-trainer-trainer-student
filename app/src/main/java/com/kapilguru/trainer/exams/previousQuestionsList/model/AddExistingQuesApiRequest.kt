package com.kapilguru.trainer.exams.previousQuestionsList.model

import com.google.gson.annotations.SerializedName

data class AddExistingQuesApiRequest(
    @SerializedName("trainer_id") var trainerId: String = "",
    @SerializedName("course_id") var courseId: Int = -1,
    @SerializedName("question_paper_id") var questionPaperId: Int = -1,
    @SerializedName("questions_json_b64") var questions_json_b64: String = "",
)
