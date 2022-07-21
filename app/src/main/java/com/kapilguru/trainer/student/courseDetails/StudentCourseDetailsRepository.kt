package com.kapilguru.trainer.student.courseDetails

import com.kapilguru.trainer.ui.courses.view_course.BatchRequest
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.view_course.ContactTrainerRequest
import com.kapilguru.trainer.ui.courses.view_course.WriteReviewRequest

class StudentCourseDetailsRepository(private val apiHelper: ApiHelper) {

    suspend fun fetchCourseDetails(courseId: String) = apiHelper.fetchCourseDetails(courseId)

    suspend fun fetchSyllabusAttachments(syllabusAttachmentId: String) = apiHelper.fetchSyllabusAttachments(syllabusAttachmentId)

    suspend fun isCourseEnrolled(courseId: String) = apiHelper.isCourseEnrolled(courseId)

    suspend fun requestBatch(batchRequest: BatchRequest) = apiHelper.requestBatch(batchRequest)

    suspend fun getStudentReviews(studentId: String) = apiHelper.getStudentReviews(studentId)

    suspend fun updateStudentReview(studentWriteReviewRequest: WriteReviewRequest) = apiHelper.updateStudentReview(studentWriteReviewRequest)

    suspend fun contactTrainer(contactTrainerRequest: ContactTrainerRequest) = apiHelper.contactTrainer(contactTrainerRequest)

}