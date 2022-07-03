package com.kapilguru.trainer.addStudent

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest

class AddStudentRepository(val apiHelper: ApiHelper) {


    suspend fun checkStudent(checkStudentRequest: CheckStudentRequest) = apiHelper.checkStudent(checkStudentRequest)
    suspend fun getBatchList(batchListApiRequest: BatchListApiRequest)=apiHelper.getBatchList(batchListApiRequest)
    suspend fun getCousesList(trainerId : String) = apiHelper.getCousesList(trainerId)

    suspend fun addOnlineStudent(onlineStudentRequest: OnlineStudentRequest) = apiHelper.addOnlineStudent(onlineStudentRequest)

}