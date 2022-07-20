package com.kapilguru.trainer.student.homeActivity.studentGallery.model

import com.google.gson.annotations.SerializedName

data class ImageResData(
    @SerializedName("name") val name: String = "",
    @SerializedName("url") val url: String = "")