package com.kapilguru.trainer.myClassRoomDetails.studymaterial.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BatchDocumentsRequest(
    @SerializedName("batch_id")
    val batchId: Int?,
    @SerializedName("trainer_id")
    val trainerId: String?
)