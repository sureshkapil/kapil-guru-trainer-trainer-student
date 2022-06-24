package com.kapilguru.trainer.student.exam.questionPaper

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.exam.model.StudentQuestionsRequest
import com.kapilguru.trainer.student.exam.model.StudentSubmitAllQuestionsRequest
import com.kapilguru.trainer.student.exam.model.StudentSubmitQuestionRequest

class StudentQuestionPaperRepository(private val apiHelper: ApiHelper) {
    suspend fun getQuestions(studentQuestionsRequest: StudentQuestionsRequest) = apiHelper.getStudentQuestions(studentQuestionsRequest)

    suspend fun submitQuestion(studentSubmitQuestionRequest: StudentSubmitQuestionRequest) = apiHelper.submitStudentQuestion(studentSubmitQuestionRequest)

    suspend fun submitAllQuestions(studentSubmitAllQuestionsRequest: StudentSubmitAllQuestionsRequest) = apiHelper.submitStudentAllQuestions(studentSubmitAllQuestionsRequest)

}