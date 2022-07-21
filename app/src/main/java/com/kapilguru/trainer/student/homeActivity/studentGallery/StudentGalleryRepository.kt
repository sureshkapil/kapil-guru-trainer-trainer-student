package com.kapilguru.trainer.student.homeActivity.studentGallery

import com.kapilguru.trainer.ApiHelper

class StudentGalleryRepository(val apiHelper: ApiHelper) {
    suspend fun getImages(packageId : String) = apiHelper.getImages(packageId)
}