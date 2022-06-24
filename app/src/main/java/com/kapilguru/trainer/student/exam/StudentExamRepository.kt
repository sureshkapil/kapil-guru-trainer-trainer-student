package com.kapilguru.trainer.student.exam

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.exam.model.StudentQuestionsRequest
import com.kapilguru.trainer.studentExamBatchResult.StudentReportRequest

class StudentExamRepository(private val apiHelper: ApiHelper) {

    suspend fun getStudentReport(studentReportRequest: StudentReportRequest) = apiHelper.getStudentReportByStudent(studentReportRequest)

    suspend fun getQuestions(studentQuestionsRequest: StudentQuestionsRequest) = apiHelper.getStudentQuestions(studentQuestionsRequest)

}