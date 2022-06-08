package com.kapilguru.trainer.ui.guestLectures.addGuestLecture

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.AddGuestLectureRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddGuestLectureRepository(private val apiHelper: ApiHelper) {
    suspend fun getCourseLanguage() = apiHelper.getCourseLanguages()
    suspend fun getCourses(trainerId: String) = apiHelper.getCousesList(trainerId)
    suspend fun addGuestLectureDetails(addGuestLectureReq: AddGuestLectureRequest) = apiHelper.addGuestLectureDetails(addGuestLectureReq)
    suspend fun addGuestLectureVideo(insertId: String, addGuestLectureReq: AddGuestLectureRequest) = apiHelper.
    addGuestLectureVideo(insertId,addGuestLectureReq)
    suspend fun addGuestLectureImage(uploadGuestLectureImage: UploadImageCourse) = apiHelper.uploadCourseImage(uploadGuestLectureImage)
    suspend fun updateGuestLecture(updateId: String, addGuestLectureReq: AddGuestLectureRequest) = apiHelper
        .updateGuestLectureDetails(updateId, addGuestLectureReq)
    suspend fun updateEditGuestLecture(updateId: String, addGuestLectureReq: AddGuestLectureRequest) = apiHelper
        .updateEditGuestLecture(updateId, addGuestLectureReq)

    suspend fun uploadVideo(files: MultipartBody.Part, trainerId: RequestBody, sourceId: RequestBody, code: RequestBody, videoType: RequestBody) =
        apiHelper.uploadVideo(files, trainerId, sourceId, code, videoType)
}