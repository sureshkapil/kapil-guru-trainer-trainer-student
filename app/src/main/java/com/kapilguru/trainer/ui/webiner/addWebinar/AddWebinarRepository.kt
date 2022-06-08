package com.kapilguru.trainer.ui.webiner.addWebinar

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.webiner.addWebinar.model.AddWebinarRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddWebinarRepository(private val apiHelper: ApiHelper) {

    suspend fun getWebinarLanguages()=apiHelper.getWebLanguages()

    suspend fun addWebinarDetails(addWebinarRequest: AddWebinarRequest)= apiHelper.addWebinarDetails(addWebinarRequest)

    suspend fun addWebinarImage(uploadWebinarImage: UploadImageCourse) = apiHelper.uploadCourseImage(uploadWebinarImage)

    suspend fun updateWebinar(insertID: String,addWebinarRequest: AddWebinarRequest)= apiHelper.updateWebinar(insertID,addWebinarRequest)

    suspend fun getWebinarDetails(webinarID: String)=apiHelper.getWebinarDetailstoEdit(webinarID)

    suspend fun updateEditWebinarDetails(webinarID: String,addWebinarRequest: AddWebinarRequest)=apiHelper.updateEditWebinarDetails(webinarID,addWebinarRequest)


    suspend fun uploadVideo(files: MultipartBody.Part, trainerId: RequestBody, sourceId: RequestBody, code: RequestBody, videoType: RequestBody) =
        apiHelper.uploadVideo(files, trainerId, sourceId, code, videoType)
}