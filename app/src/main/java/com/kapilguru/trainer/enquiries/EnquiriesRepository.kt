package com.kapilguru.trainer.enquiries

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.enquiries.addOfflineEnquiry.data.AddEnquiryReq

class EnquiriesRepository(private val apiHelper: ApiHelper) {
    suspend fun getEnquiries(trainerId: String) = apiHelper.getEnquiries(trainerId)
    suspend fun getCoursesList(trainerId: String) = apiHelper.getCousesList(trainerId)
    suspend fun addEnquiry(addEnquiryReq: AddEnquiryReq) = apiHelper.addEnquiry(addEnquiryReq)
}