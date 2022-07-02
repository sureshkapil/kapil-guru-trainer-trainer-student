package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class ResponseStudentModel(
    @SerializedName("data") var responseStudentModelApi: List<StudentModelResponseApi>?, @SerializedName("message") var message: String? = "", @SerializedName("status") var status: Int? = 0
)