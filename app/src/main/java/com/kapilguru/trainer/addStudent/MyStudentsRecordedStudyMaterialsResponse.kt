package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class MyStudentsRecordedStudyMaterialsResponse(
    @SerializedName("data") val myStudentsRecordedStudyMaterialsResponseApi: List<MyStudentsRecordedStudyMaterialsResponseApi>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)