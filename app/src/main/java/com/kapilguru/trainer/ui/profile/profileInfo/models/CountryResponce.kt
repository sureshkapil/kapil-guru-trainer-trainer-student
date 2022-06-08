package com.kapilguru.trainer.ui.profile.profileInfo.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CountryResponce(
    @SerializedName("data") val countryList: List<CountryResponceItem>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)
