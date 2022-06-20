package com.kapilguru.trainer.student.profileInfo

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.profile.data.StudentProfileData
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.profile.data.CheckOTPRequest
import com.kapilguru.trainer.ui.profile.data.GetOTPRequest

class StudentProfileInfoRepository(private val apiHelper: ApiHelper) {
    suspend fun getCountryList() = apiHelper.getStudentCountryList()
    suspend fun getSateList(countryId: Int) = apiHelper.getStudentStateList(countryId)
    suspend fun getCityList(stateId: Int) = apiHelper.getStudentCityList(stateId)
    suspend fun getStudentProfileData(userId: String) = apiHelper.getStudentProfileData(userId)
    suspend fun updateStudentProfile(userId: String, studentProfileData: StudentProfileData) = apiHelper.updateStudentProfileData(userId, studentProfileData)
    suspend fun getOTRequest(getOTPRequest: GetOTPRequest) = apiHelper.generateOTP(getOTPRequest)
    suspend fun checkOTP(checkOTPRequest: CheckOTPRequest) = apiHelper.checkOTP(checkOTPRequest)
    suspend fun uploadCourseImage(uploadImageCourse: UploadImageCourse) = apiHelper.uploadCourseImage(uploadImageCourse)
}