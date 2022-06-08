package com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data

import com.google.gson.annotations.SerializedName

data class LanguageResponce(
    @SerializedName("status") val status : Int,
    @SerializedName("message") val message : String,
    @SerializedName("data") val languageData : ArrayList<LanguageData>
)
