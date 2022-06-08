package com.kapilguru.trainer.allSubscription.positionSubscription

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.allSubscription.positionSubscription.model.CoursePositionMapRequest

class PositionSubscriptionRepository(private val apiHelper: ApiHelper) {
    suspend fun getPositionSubscriptionList(trainerId: String) = apiHelper.getTrainerCourseData(trainerId)
    suspend fun mapCoursePosition(coursePositionMapRequest : CoursePositionMapRequest) = apiHelper.mapCoursePosition(coursePositionMapRequest)
}