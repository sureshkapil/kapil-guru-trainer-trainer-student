package com.kapilguru.trainer.addStudent

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.addStudent.AddofflineStudent.AddOfflineStudentRequest
import com.kapilguru.trainer.addStudent.addOnlineStudent.OnlineStudentRequest
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest

class AddStudentRepository(val apiHelper: ApiHelper) {


    suspend fun checkStudent(checkStudentRequest: CheckStudentRequest) = apiHelper.checkStudent(checkStudentRequest)
    suspend fun getBatchList(batchListApiRequest: BatchListApiRequest)=apiHelper.getBatchList(batchListApiRequest)
    suspend fun getCousesList(trainerId : String) = apiHelper.getCousesList(trainerId)

    suspend fun addOnlineStudent(onlineStudentRequest: OnlineStudentRequest) = apiHelper.addOnlineStudent(onlineStudentRequest)


    suspend fun addOfflineStudent(addOfflineStudentRequest: AddOfflineStudentRequest) = apiHelper.addOfflineStudent(addOfflineStudentRequest)


    suspend fun getMyCourseStudents(trainerId: String)= apiHelper.getMyCourseStudents(trainerId)

    suspend fun getMyRecordedStudents(trainerId: String)= apiHelper.getMyRecordedStudents(trainerId)

    suspend fun geOfflineStudents(trainerId: String)= apiHelper.geOfflineStudents(trainerId)


    suspend fun getSignedUpStudentsList(trainerId: String)= apiHelper.getSignedUpStudentsList(trainerId)



}