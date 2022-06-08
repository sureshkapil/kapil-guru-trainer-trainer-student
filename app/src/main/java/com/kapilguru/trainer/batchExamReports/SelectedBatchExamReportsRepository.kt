package com.kapilguru.trainer.batchExamReports

import com.kapilguru.trainer.ApiHelper

class SelectedBatchExamReportsRepository(private val apiHelper: ApiHelper) {

    suspend fun batchStudentReport(batchReportsRequestModel: BatchReportsRequestModel) = apiHelper.batchStudentReport(batchReportsRequestModel)

}