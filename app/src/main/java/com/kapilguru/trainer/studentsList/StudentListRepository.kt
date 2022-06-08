package com.kapilguru.trainer.studentsList

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.studentsList.model.RequestRaiseComplaint


class StudentListRepository(private val apiHelper: ApiHelper) {

    suspend fun getTrainerStudentList(trainerId: String) = apiHelper.getTrainerStudentList(trainerId)

    suspend fun getTrainerCourseStudentList(courseId: String) = apiHelper.getTrainerCourseStudentList(courseId)

    suspend fun fetchStudentListForBatchApi(batchId: String) = apiHelper.fetchStudentListForBatchApi(batchId)

    suspend fun getWebinarStudentsList(webinarId : String) = apiHelper.getWebinarStudentsList(webinarId)

    suspend fun postComplaint(request: RequestRaiseComplaint) = apiHelper.postComplaint(request)

}