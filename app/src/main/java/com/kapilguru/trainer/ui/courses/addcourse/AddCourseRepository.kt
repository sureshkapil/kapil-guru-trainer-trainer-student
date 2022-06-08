package com.kapilguru.trainer.ui.courses.addcourse

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.addcourse.models.AddCourseRequest
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddCourseRepository(private val apiHelper: ApiHelper) {

    suspend fun saveAndNext(addCourseRequest: AddCourseRequest) = apiHelper.postCourse(addCourseRequest)


    suspend fun updateCourse(courseId: String,addCourseRequest: AddCourseRequest) = apiHelper.updateCourse(courseId,addCourseRequest)

    suspend fun getCourseDetails(courseId: String) = apiHelper.getCourseDetails(courseId)


    suspend fun uploadCourseImage(uploadImageCourse: UploadImageCourse) = apiHelper.uploadCourseImage(uploadImageCourse)

    suspend fun uploadVideo(files: MultipartBody.Part, trainerId: RequestBody, sourceId: RequestBody, code: RequestBody, videoType: RequestBody) =
        apiHelper.uploadVideo(files, trainerId, sourceId, code, videoType)

    suspend fun uploadCoursePdfFile(file: MultipartBody.Part, trainerId: RequestBody, courseId: RequestBody, pdfName: RequestBody) = apiHelper.uploadCoursePdfFile(file,trainerId,courseId,pdfName)

    suspend fun getCoursePdfFile(pdfId: String) = apiHelper.getCoursePdfFile(pdfId)

    suspend fun fetchCategory() = apiHelper.getCategory()


    suspend fun getCourseLanguage() = apiHelper.getCourseLanguages()

}