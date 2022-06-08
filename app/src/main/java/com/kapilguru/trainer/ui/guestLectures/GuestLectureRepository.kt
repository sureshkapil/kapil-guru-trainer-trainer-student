package com.kapilguru.trainer.ui.guestLectures

import com.kapilguru.trainer.ApiHelper

class GuestLectureRepository(private val apiHelper: ApiHelper) {
    suspend fun getGuestLectureList(trainerId: String)=apiHelper.getGuestlectureList(trainerId)
    suspend fun getLiveUpComingGuestLectureList(trainerId: String)=apiHelper.getLiveUpComingGuestLectureList(trainerId)
    suspend fun deleteGuestLecture(guestLectureDeleteId: String)= apiHelper.deleteGuestLecture(guestLectureDeleteId)
}