package com.kapilguru.trainer.myClassroom

import com.kapilguru.trainer.ApiHelper

class MyClassroomRepository(val apiHelper: ApiHelper) {
    suspend fun getBatchList(userId: String) = apiHelper.getBatchList(userId)
    suspend fun getLiveUpComingClasses(trainerId: String) = apiHelper.getLiveUpComingClasses(trainerId)

}