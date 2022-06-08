package com.kapilguru.trainer.ui.profile.profileInfo.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class
CityResponceItem(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("state_id") val country_id: Int? = null,
    @SerializedName("is_active") val is_active: Int? = null){
    override fun toString(): String {
        return name!!
    }
}
