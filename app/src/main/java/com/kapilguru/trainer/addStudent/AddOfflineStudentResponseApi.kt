package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class AddOfflineStudentResponseApi(
    @SerializedName("insertId") val insertId: Int? = 0
)