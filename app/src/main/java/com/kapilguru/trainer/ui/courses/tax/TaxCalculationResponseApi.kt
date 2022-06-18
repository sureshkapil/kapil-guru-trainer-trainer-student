package com.kapilguru.trainer.ui.courses.tax

import com.google.gson.annotations.SerializedName

data class TaxCalculationResponseApi(
    @SerializedName("valid_upto") val validUpto: String? = null,
    @SerializedName("is_active") val isActive: Int? = 0,
    @SerializedName("name") val name: String? = "",
    @SerializedName("modified_by") val modifiedBy: String? = null,
    @SerializedName("id") val id: Int ?= 0,
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("add_percent") val addPercent: Double = 0.0,
    @SerializedName("modified_date") val modifiedDate: String? = null,
)