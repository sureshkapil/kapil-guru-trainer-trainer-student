package com.kapilguru.trainer.myClassRoomDetails.completionRequest

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.BatchCompletionRequest
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.SendBatchCompletionRequest
import com.kapilguru.trainer.myClassRoomDetails.studymaterial.model.BatchDocumentsRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BatchCompletionReqRepository(private val apiHelper : ApiHelper) {

suspend fun getBatchCompletionResponse(batchCompletionReq : BatchCompletionRequest) = apiHelper.getBatchCompletionResponse(batchCompletionReq)

    suspend fun sendBatchCompletionRequest(sendBatchCompletionRequest: SendBatchCompletionRequest) = apiHelper.sendBatchCompletionRequest(sendBatchCompletionRequest)

    suspend fun getBatchDetails(batchId: String) = apiHelper.getBatchDetails(batchId)

    suspend fun getBatchPdfDocuments(batchDocumentsRequest: BatchDocumentsRequest) = apiHelper.getBatchDocuments(batchDocumentsRequest)

    suspend fun downloadPdfDocument(fileName: String) = apiHelper.downloadPdfDocument(fileName)

    suspend fun uploadBatchPdfFile(file: MultipartBody.Part, trainerId: RequestBody, pdfName: RequestBody, courseId: RequestBody, batchId: RequestBody) = apiHelper.uploadBatchPdfFile(file, trainerId, pdfName, courseId, batchId)

}