package com.kapilguru.trainer.ui.courses.addcourse.models

import com.google.gson.annotations.SerializedName

data class UploadImageCourse(
    @SerializedName("code") var code: String = "",
    @SerializedName("source_type") var sourceType: String = "",
    @SerializedName("source_id") var sourceId: Int = 0,
    @SerializedName("base64Image") var baseImage: String = "",
)