package com.kapilguru.trainer.exams.scheduledExams

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaperTitleRequest

class ScheduleExamsRepository(private val apiHelper : ApiHelper) {

    suspend fun getScheduleList(trainerId : String) = apiHelper.getScheduleList(trainerId)
}