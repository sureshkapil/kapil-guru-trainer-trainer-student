package com.kapilguru.trainer.allSubscription.bestTrainerSubscription

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.CourseBestTrainerMapRequest

class BestTrainerCourseRepository(private val apiHelper: ApiHelper) {
    suspend fun getBestTrainerCourseList(trainerId : String) = apiHelper.getBestTrainerCourseList(trainerId)
    suspend fun mapCourseBestTrainer(course3BestTrainerMapReq : CourseBestTrainerMapRequest) = apiHelper.mapCourseBestTrainer(course3BestTrainerMapReq)
}