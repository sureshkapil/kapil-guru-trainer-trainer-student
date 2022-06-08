package com.kapilguru.trainer.myClassroom.liveClasses.model

import com.google.gson.annotations.SerializedName

data class LiveUpComingClassResponse(
    @SerializedName("data") val liveUpComingClassData: List<LiveUpComingClassData>? = null,
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: Int? = 0)