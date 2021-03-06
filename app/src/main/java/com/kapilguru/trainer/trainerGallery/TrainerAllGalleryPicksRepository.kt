package com.kapilguru.trainer.trainerGallery

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.trainer.signup.model.register.RegisterRequest
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOptSmsResponse
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOtpSmsRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.trainer.signup.model.validateOtp.ValidateOtpRequest
import java.util.*

class TrainerAllGalleryPicksRepository(private val apiHelper : ApiHelper) {

    suspend fun uploadImage(uploadImageCourse: UploadImageGallery) = apiHelper.uploadTrainerGalleryImages(uploadImageCourse)


    suspend fun getImagesList(packageId: String) = apiHelper.getTrainerGalleryImages(packageId)

    suspend  fun deleteTrainerGalleryImages(code: String,imageName: String) =  apiHelper.deleteTrainerGalleryImages(code,imageName)

}