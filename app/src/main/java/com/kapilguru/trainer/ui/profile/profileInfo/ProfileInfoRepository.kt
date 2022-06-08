package com.kapilguru.trainer.ui.profile.profileInfo

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.profile.data.CheckOTPRequest
import com.kapilguru.trainer.ui.profile.data.GetOTPRequest
import com.kapilguru.trainer.ui.profile.data.ProfileData
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileInfoRepository(private val apiHelper: ApiHelper) {
    suspend fun getCountryList() = apiHelper.getCountryList()
    suspend fun getSateList(countryId: Int) = apiHelper.getStateList(countryId)
    suspend fun getCityList(stateId: Int) = apiHelper.getCityList(stateId)
    suspend fun saveProfile(profileData: ProfileData) = apiHelper.saveProfileData(profileData)
    suspend fun getProfileData(userId: String) = apiHelper.getProfileData(userId)
    suspend fun updateProfile(userId: String, profileData: ProfileData) = apiHelper.updateProfileData(userId, profileData)
    suspend fun getOTRequest(getOTPRequest : GetOTPRequest) = apiHelper.generateOTP(getOTPRequest)
    suspend fun checkOTP(checkOTPRequest: CheckOTPRequest) = apiHelper.checkOTP(checkOTPRequest)
    suspend fun uploadCourseImage(uploadImageCourse: UploadImageCourse) = apiHelper.uploadCourseImage(uploadImageCourse)
    suspend fun uploadAadharPdfFile(file: MultipartBody.Part, userId: RequestBody, title: RequestBody) = apiHelper.uploadAadharPdfFile(file,userId,title)
    suspend fun uploadPanPdfFile(file: MultipartBody.Part, userId: RequestBody, title: RequestBody) = apiHelper.uploadPanPdfFile(file,userId,title)
}