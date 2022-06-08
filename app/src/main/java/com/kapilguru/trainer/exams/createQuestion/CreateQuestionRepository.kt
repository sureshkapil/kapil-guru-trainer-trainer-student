package com.kapilguru.trainer.exams.createQuestion

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.createQuestion.model.AddQuestionRequest
import com.kapilguru.trainer.exams.createQuestion.model.UpdateQuestionRequest

class CreateQuestionRepository(private val apiHelper: ApiHelper) {
    suspend fun addQuestion(addQuestionRequest: AddQuestionRequest) = apiHelper.addQuestion(addQuestionRequest)
    suspend fun updateQuestion(questionId: String, updateQuestionRequest: UpdateQuestionRequest) = apiHelper.updateQuestion(questionId, updateQuestionRequest)
}