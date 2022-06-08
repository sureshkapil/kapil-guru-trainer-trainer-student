package com.kapilguru.trainer.myClassRoomDetails.completionRequest.model

import com.google.gson.annotations.SerializedName

data class BatchCompletionResBCReq(
    @SerializedName("student_id") var studentId: Int = -1,
    @SerializedName("student_name") var studentName: String = "",
    @SerializedName("batch_id") var batchId: Int = -1,
    @SerializedName("bcr_req_id") var bcrReqId: Int = -1,
    @SerializedName("bcr_req_resp_date") var bcrReqRespDate: String = "",
    @SerializedName("bcr_req_resp_status") var bcrReqRespStatus: String = "",
    @SerializedName("bcr_req_resp_reason") var bcrReqRespReason: String = ""
)