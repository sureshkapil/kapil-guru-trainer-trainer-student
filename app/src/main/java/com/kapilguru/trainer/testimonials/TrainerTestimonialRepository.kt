package com.kapilguru.trainer.testimonials

import com.kapilguru.trainer.ApiHelper

class TrainerTestimonialRepository(private val apiHelper: ApiHelper) {

    suspend fun addtestimonials(addTrainerTestimonial: PostTestimonialsModel) = apiHelper.addtestimonials(addTrainerTestimonial)

    suspend fun getAllTestimonials(tenantId: Int) = apiHelper.getTestimonials(tenantId)

}