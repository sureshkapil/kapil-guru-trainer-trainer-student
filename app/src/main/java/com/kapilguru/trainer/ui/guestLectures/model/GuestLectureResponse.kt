package com.kapilguru.trainer.ui.guestLectures.model

import com.google.gson.annotations.SerializedName

data class GuestLectureResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : List<GuestLectureData>
)