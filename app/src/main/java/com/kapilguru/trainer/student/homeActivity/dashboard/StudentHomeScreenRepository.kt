package com.kapilguru.trainer.student.homeActivity.dashboard

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.homeActivity.models.CreateLeadRequest


class StudentHomeScreenRepository(private val apiHelper: ApiHelper) {

    suspend fun fetchUpcomingSchedule(userId: String) = apiHelper.fetchUpcomingSchedule(userId)


    suspend fun fetchAllWebinars() = apiHelper.fetchAllWebinars()

    suspend fun fetchAllDemos() = apiHelper.fetchAllDemos()

    suspend fun fetchAllJobOpenings() = apiHelper.getAllJobOpenings()

    suspend fun getDashBoardPopularAndTrendingCourses() = apiHelper.getDashBoardPopularAndTrendingCourses()

    suspend fun getLiveCourses(parentTrainerId: String) = apiHelper.getLiveCourses(parentTrainerId)

    suspend fun getAllPopularAndTrendingCourses() = apiHelper.getAllPopularAndTrendingCourses()

    suspend fun createLeadApi(createLeadRequest: CreateLeadRequest) = apiHelper.createLeadApi(createLeadRequest)

    suspend fun getImages(packageId : String) = apiHelper.getImages(packageId)


}