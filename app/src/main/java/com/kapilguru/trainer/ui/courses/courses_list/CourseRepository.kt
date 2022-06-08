package com.kapilguru.trainer.ui.courses.courses_list

import com.kapilguru.trainer.ApiHelper

class CourseRepository(private val apiHelper: ApiHelper) {

    suspend fun getCousesList(trainerId : String) = apiHelper.getCousesList(trainerId)

    suspend fun deleteCourse(courseId : String) = apiHelper.deleteCourse(courseId)
}