package com.kapilguru.trainer.testimonials

import android.app.Application
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.trainer.signup.model.register.RegisterRequest
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOptSmsResponse
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOtpSmsRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.trainer.signup.model.validateOtp.ValidateOtpRequest
import java.util.*

class TrainerTestimonialRepository(private val apiHelper: ApiHelper) {

    suspend fun addtestimonials(addTrainerTestimonial: PostTestimonialsModel) = apiHelper.addtestimonials(addTrainerTestimonial)

}