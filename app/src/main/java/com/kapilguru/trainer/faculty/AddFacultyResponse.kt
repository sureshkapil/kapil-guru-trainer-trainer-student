package com.kapilguru.trainer.faculty

import com.google.gson.annotations.SerializedName

data class AddFacultyResponse(
    @SerializedName("data") var data: AddFacultyResponseApi?, @SerializedName("message") var message: String? = "", @SerializedName("status") var status: Int? = 0
)