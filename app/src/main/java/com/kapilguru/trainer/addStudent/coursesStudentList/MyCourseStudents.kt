package com.kapilguru.trainer.addStudent.coursesStudentList

import com.google.gson.annotations.SerializedName

data class MyCourseStudents(
    @SerializedName("data") var myStudentsOnlineApi: List<MyCourseStudentsApi>?, @SerializedName("message") var message: String? = "", @SerializedName("status") var status: Int? = 0
)