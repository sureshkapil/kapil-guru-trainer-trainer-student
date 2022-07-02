package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class StudentModelResponseApi(@SerializedName("student_name")
                    val studentName: String = "",
                                   @SerializedName("student_id")
                    val studentId: Int = 0)