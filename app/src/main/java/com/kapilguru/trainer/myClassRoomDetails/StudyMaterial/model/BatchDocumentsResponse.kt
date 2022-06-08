package com.kapilguru.trainer.myClassRoomDetails.studymaterial.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class BatchDocumentsResponse(
    @SerializedName("data")
    val `data`: ArrayList<BatchDocumentData>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)