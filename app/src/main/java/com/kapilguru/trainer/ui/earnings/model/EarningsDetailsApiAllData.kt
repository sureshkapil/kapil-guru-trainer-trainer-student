package com.kapilguru.trainer.ui.earnings.model

import com.google.gson.annotations.SerializedName

data class EarningsDetailsApiAllData(
    @SerializedName("available") var available: String,
    @SerializedName("expected") var expected: String,
    @SerializedName("received") var received: String,
    @SerializedName("requested") var requested: String,
    @SerializedName("user_id") var userId: String
)