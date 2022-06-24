package com.kapilguru.trainer.student.myClassroom

import com.kapilguru.trainer.ApiHelper

class StudentMyClassroomRepository(val apiHelper: ApiHelper) {
    suspend fun getAllStudentClasses(userId: String) = apiHelper.getAllStudentClasses(userId)
}