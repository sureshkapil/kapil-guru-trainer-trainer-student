package com.kapilguru.trainer.exams.previousQuestionPapersList

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.CopyFromQuesPaperRequest

class PreviousQuestionPapersListRepository(private val apiHelper: ApiHelper) {
    suspend fun getPreviousQuestionPapersList(trainerId: String, courseId: String) = apiHelper.getPreviousQuestionPapersList(trainerId, courseId)
    suspend fun getQuestionsList(trainerId :String) = apiHelper.getQuestionsList(trainerId)
    suspend fun callCopyFromQuestionPaper(copyFromQuesPaperRequest: CopyFromQuesPaperRequest) = apiHelper.callCopyFromQuestionPaper(copyFromQuesPaperRequest)

}