package com.kapilguru.trainer.ui.courses.add_batch

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchRequest

class AddBatchRepository(private  val apiHelper: ApiHelper) {
    suspend fun postAddBatch(addBatchApiRequest: AddBatchRequest) = apiHelper.postAddBatch(addBatchApiRequest)

    suspend fun updateBatch(batchId: Int,addBatchApiRequest: AddBatchRequest) = apiHelper.updateBatch(batchId,addBatchApiRequest)

    suspend fun editBatch(batchId: Int) = apiHelper.getEditBatch(batchId)
}