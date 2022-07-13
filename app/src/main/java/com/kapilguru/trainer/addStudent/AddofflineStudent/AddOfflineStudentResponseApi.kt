package com.kapilguru.trainer.addStudent.AddofflineStudent

import com.google.gson.annotations.SerializedName

data class AddOfflineStudentResponseApi(
    @SerializedName("insertId") val insertId: Int? = 0
)