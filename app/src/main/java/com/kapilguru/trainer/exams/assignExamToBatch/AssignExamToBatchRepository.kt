package com.kapilguru.trainer.exams.assignExamToBatch

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.assignExamToBatch.model.AssignExamToBatchRequest

class AssignExamToBatchRepository(private val apiHelper: ApiHelper) {
    suspend fun getActiveBatchesByCourse(trainerId :String, courseId :String) = apiHelper.getActiveBatchesByCourse(trainerId,courseId)
    suspend fun assignExamToBatch(assignExamToBatchRequest: AssignExamToBatchRequest) = apiHelper.assignExamToBatch(assignExamToBatchRequest)
}