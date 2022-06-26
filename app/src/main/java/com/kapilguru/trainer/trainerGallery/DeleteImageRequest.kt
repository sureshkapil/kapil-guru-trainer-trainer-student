package com.kapilguru.trainer.trainerGallery

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.BuildConfig

data class DeleteImageRequest(
    @SerializedName("code") val code: String? = BuildConfig.PACKAGE_ID
)