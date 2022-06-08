package com.kapilguru.trainer.exams

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaperTitleRequest

class ExamsRepository(private val apiHelper : ApiHelper) {

    suspend fun getCoursesList(trainerId : String) = apiHelper.getCousesList(trainerId)

    suspend fun sendQuestionPaperTitle(questionPaperTitle : QuestionPaperTitleRequest) = apiHelper.sendQuestionPaperTitle(questionPaperTitle)

    suspend fun getScheduleList(trainerId : String) = apiHelper.getScheduleList(trainerId)
}