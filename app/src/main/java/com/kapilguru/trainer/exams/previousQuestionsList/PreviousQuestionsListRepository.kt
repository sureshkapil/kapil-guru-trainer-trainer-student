package com.kapilguru.trainer.exams.previousQuestionsList

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.previousQuestionsList.model.AddExistingQuesApiRequest

class PreviousQuestionsListRepository(private val apiHelper: ApiHelper) {
    suspend fun getPreviousQuestionsList(trainerId: String, courseId: String) = apiHelper.getPreviousQuestionsList(trainerId,courseId)
    suspend fun addExistingQuestions(addExistingQuesApiRequest: AddExistingQuesApiRequest) = apiHelper.addExistingQuestions(addExistingQuesApiRequest)

}