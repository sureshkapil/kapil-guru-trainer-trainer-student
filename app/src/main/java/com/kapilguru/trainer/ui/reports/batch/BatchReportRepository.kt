package com.kapilguru.trainer.ui.reports.batch

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest

class BatchReportRepository(private val apiHelper : ApiHelper) {
    suspend fun getBatchList(batchListApiRequest: BatchListApiRequest)=apiHelper.getBatchList(batchListApiRequest)

}