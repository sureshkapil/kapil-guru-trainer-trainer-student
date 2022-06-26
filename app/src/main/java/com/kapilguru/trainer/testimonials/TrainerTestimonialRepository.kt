package com.kapilguru.trainer.testimonials

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.network.CommonResponseApi

class TrainerTestimonialRepository(private val apiHelper: ApiHelper) {

    suspend fun addtestimonials(addTrainerTestimonial: PostTestimonialsModel) = apiHelper.addtestimonials(addTrainerTestimonial)

    suspend fun getAllTestimonials(tenantId: Int) = apiHelper.getTestimonials(tenantId)

    suspend  fun updateTestimonialStatus(id: String, testimonialApproveRequest: TestimonialApproveRequest) = apiHelper.updateTestimonialStatus(id, testimonialApproveRequest)

    suspend  fun deleteTestimonial(id: String) = apiHelper.deleteTestimonial(id)


}