package com.kapilguru.trainer.student.myClassRoomDetails

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListRequest
import com.kapilguru.trainer.student.myClassRoomDetails.model.RaiseComplaintByStudentRequest
import com.kapilguru.trainer.student.myClassRoomDetails.model.RefundStudentRequest
import com.kapilguru.trainer.student.myClassRoomDetails.model.ReviewStudentRequest


class StudentMyClassRoomDetailsRepository(private val apiHelper: ApiHelper) {

    suspend fun getBatchDetails(batchId: String) = apiHelper.getStudentBatchDetails(batchId)

    suspend fun raiseComplaintByStudent(request: RaiseComplaintByStudentRequest) = apiHelper.raiseComplaintByStudent(request)

    suspend fun requestRefundByStudent(request: RefundStudentRequest) = apiHelper.requestRefundByStudent(request)

    suspend fun updateReviewByStudent(reviewStudentRequest: ReviewStudentRequest) = apiHelper.updateReviewByStudent(reviewStudentRequest)

    suspend fun getStudentExamList(studentQuestionPaperListRequest: StudentQuestionPaperListRequest) = apiHelper.getStudentExamList(studentQuestionPaperListRequest)

    suspend fun getStudentStudyMaterialList(batchId: String) = apiHelper.getStudentStudyMaterialList(batchId)
}