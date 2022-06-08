package com.kapilguru.trainer.exams.conductExams.createQuestionPaper

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaperTitleRequest

class CreateQuestionPaperRepository(private val apiHelper : ApiHelper ) {
 suspend fun getQuestionsList(trainerId :String) = apiHelper.getQuestionsList(trainerId)
}