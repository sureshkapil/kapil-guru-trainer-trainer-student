package com.kapilguru.trainer.ui.courses.batchesList

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest

class BatchListRepository(private val apiHelper: ApiHelper) {

    suspend fun getBatchList(batchListApiRequest: BatchListApiRequest)=apiHelper.getBatchList(batchListApiRequest)

    suspend fun deleteBatch(batchId: String)=apiHelper.deleteBatch(batchId)
}