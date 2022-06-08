package com.kapilguru.trainer.ui.profile.profileInfo.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CityResponce(
    @SerializedName("data") val cityList: List<CityResponceItem>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)

