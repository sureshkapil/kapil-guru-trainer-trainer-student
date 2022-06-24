package com.kapilguru.trainer.student.allExamsList

import com.kapilguru.trainer.ApiHelper

class AllStudentExamsListRepository(private val apiHelper: ApiHelper) {

    suspend fun getAllStudentExamsList(studentId: String) = apiHelper.getAllStudentExamsList(studentId)
}