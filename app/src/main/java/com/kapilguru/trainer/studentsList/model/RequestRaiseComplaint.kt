package com.kapilguru.trainer.studentsList.model

import com.google.gson.annotations.SerializedName

data class RequestRaiseComplaint(
    @SerializedName("on_whom")
    var onWhom: Int = 0,
    @SerializedName("user_id")
    var userId: Int = 0,
    @SerializedName("text")
    var text: String = ""
)