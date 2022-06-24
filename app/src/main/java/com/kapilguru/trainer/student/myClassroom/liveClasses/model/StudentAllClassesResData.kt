package com.kapilguru.trainer.student.myClassroom.liveClasses.model

import com.google.gson.annotations.SerializedName

data class StudentAllClassesResData(
    @SerializedName("active") val activeClassesString : String,
    @SerializedName("ongoing") val liveUpComingClassesString : String,
    )
