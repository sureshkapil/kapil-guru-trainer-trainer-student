package com.kapilguru.trainer.student.homeActivity.studentGallery.model

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("data") val imageList: ArrayList<ImageResData>?,
    @SerializedName("status") val status: Int = 0)