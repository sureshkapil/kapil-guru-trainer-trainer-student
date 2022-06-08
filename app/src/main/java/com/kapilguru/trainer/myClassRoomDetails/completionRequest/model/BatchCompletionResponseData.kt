package com.kapilguru.trainer.myClassRoomDetails.completionRequest.model

import com.google.gson.annotations.SerializedName

data class BatchCompletionResponseData(
    @SerializedName("summary") val summary : String,
    @SerializedName("bcr_req") val bcrReq : String,
) {
}