package com.kapilguru.trainer.allSubscription.models

import com.google.gson.annotations.SerializedName

data class UpdateKycRequest(
    @SerializedName("pan") var pan: String = "", //Mandatory
    @SerializedName("gst") var gst: String? = null, //Not mandatory
)