package com.kapilguru.trainer.demo_webinar_students

import com.kapilguru.trainer.ApiHelper

class DemoWebinarStudentRepository(private val apiHelper: ApiHelper) {

    suspend fun getDemoWebinarStudentList(trainerId: String)=apiHelper.getDemoWebinarStudents(trainerId)

    suspend fun getDemoStudentListApi(trainerId: String)=apiHelper.getDemoStudentListApi(trainerId)

}