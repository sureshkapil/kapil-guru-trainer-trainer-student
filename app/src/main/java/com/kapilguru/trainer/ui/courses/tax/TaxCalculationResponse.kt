package com.kapilguru.trainer.ui.courses.tax

import com.google.gson.annotations.SerializedName

data class TaxCalculationResponse(
    @SerializedName("data") var data: List<TaxCalculationResponseApi>?, @SerializedName("message") var message: String? = "", @SerializedName("status") var status: Int? = 0
)