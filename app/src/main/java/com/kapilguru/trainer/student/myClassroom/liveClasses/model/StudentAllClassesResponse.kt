package com.kapilguru.trainer.student.myClassroom.liveClasses.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentAllClassesResData

data class StudentAllClassesResponse(
    @SerializedName("allData") val allClassesData: StudentAllClassesResData,
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: Int? = 0)