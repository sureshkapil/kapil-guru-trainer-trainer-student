package com.kapilguru.trainer.ui.courses.view_course


import com.kapilguru.trainer.ApiHelper

class CourseDetailsRepository(private val apiHelper: ApiHelper) {

    suspend fun fetchCourseDetails(courseId: String) = apiHelper.fetchCourseDetails(courseId)

    suspend fun fetchSyllabusAttachments(syllabusAttachmentId: String) = apiHelper.fetchSyllabusAttachments(syllabusAttachmentId)


    suspend fun requestBatch(batchRequest: BatchRequest) =  apiHelper.requestBatch(batchRequest)

    suspend fun getStudentReviews(studentId: String) = apiHelper.getStudentReviews(studentId)

//    suspend fun updateStudentReview(writeReviewRequest: WriteReviewRequest) = apiHelper.updateStudentReview(writeReviewRequest)

    suspend fun contactTrainer(contactTrainerRequest: ContactTrainerRequest) = apiHelper.contactTrainer(contactTrainerRequest)

}