package com.kapilguru.trainer.studentExamBatchResult

import com.kapilguru.trainer.ApiHelper

class StudentExamBatchResultRepository(val apiHelper: ApiHelper) {

    suspend fun getStudentReport(studentReportRequest:StudentReportRequest) = apiHelper.getStudentReport(studentReportRequest)

    suspend fun getAnswerSheet(studentReportRequest:StudentExamPaperRequest) = apiHelper.getAnswerSheet(studentReportRequest)

}