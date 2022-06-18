package com.kapilguru.trainer.trainerGallery

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.BuildConfig

data class UploadImageGallery(
    @SerializedName("code") var code: String = BuildConfig.PACKAGE_ID,
    @SerializedName("tenant_id") var tenantId: Int,
    @SerializedName("source_id") var sourceId: Int,
    @SerializedName("base64Image") var baseImage: String = "",
)