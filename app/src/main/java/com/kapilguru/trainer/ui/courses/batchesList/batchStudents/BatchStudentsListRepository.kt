package com.kapilguru.trainer.ui.courses.batchesList.batchStudents

import com.kapilguru.trainer.ApiHelper

class BatchStudentsListRepository( private val apiHelper: ApiHelper) {

    suspend fun getBatchStudentList(batchId: String) = apiHelper.getBatchStudentList(batchId)
}