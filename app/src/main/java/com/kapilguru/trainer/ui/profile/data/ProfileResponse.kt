package com.kapilguru.trainer.ui.profile.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileResponse(
    @SerializedName("data") val data: List<ProfileData> ? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)
