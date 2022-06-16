package com.kapilguru.trainer.trainerGallery

import com.google.gson.annotations.SerializedName

data class TrainerGalleryImagesResponseApi(
    @SerializedName("name") var name: String? = "",
    @SerializedName("url") var url: String? = ""
)