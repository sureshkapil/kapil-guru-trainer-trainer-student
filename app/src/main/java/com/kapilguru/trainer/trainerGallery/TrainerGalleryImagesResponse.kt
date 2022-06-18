package com.kapilguru.trainer.trainerGallery

import com.google.gson.annotations.SerializedName

data class TrainerGalleryImagesResponse(
    @SerializedName("data") val trainerGalleryImagesResponseApi: List<TrainerGalleryImagesResponseApi>?,
    @SerializedName("status") val status: Int = 0
)