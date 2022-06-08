package com.kapilguru.trainer.ui.guestLectures.guestLectureStudent

import com.kapilguru.trainer.ApiHelper

class GuestLectureStudentViewRepository(private val apiHelper: ApiHelper) {
    suspend fun getGuestLectureStudentViewDetails(demoLectureId: String) = apiHelper.getGuestLectureStudentViewDetails(demoLectureId)
}