package com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data

import com.google.gson.annotations.SerializedName

data class AddGuestLectureImageRequest(
    @SerializedName("code") var code: String = "",
    @SerializedName("base64Image") var baseImage: String = "",
    @SerializedName("source_type") var sourceType: String = "",
    @SerializedName("source_id") var sourceId: Int = 0
)