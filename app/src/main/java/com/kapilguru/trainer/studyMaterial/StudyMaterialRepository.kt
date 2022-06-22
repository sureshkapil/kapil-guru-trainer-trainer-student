package com.kapilguru.trainer.studyMaterial

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.trainer.signup.model.register.RegisterRequest
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOptSmsResponse
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOtpSmsRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.trainer.signup.model.validateOtp.ValidateOtpRequest
import java.util.*

class StudyMaterialRepository(private val apiHelper : ApiHelper) {


 suspend fun getListOfStudyMaterials(studyMaterialListRequest: StudyMatrialListRequest) = apiHelper.getListOfStudyMaterials(studyMaterialListRequest)

//    suspend fun getStudyMaterialList(studyMaterialListRequest: StudyMaterialListRequest) = apiHelper.getStudyMaterialList(studyMaterialListRequest)


    suspend fun getImagesList(packageId: String) = apiHelper.getTrainerGalleryImages(packageId)




}