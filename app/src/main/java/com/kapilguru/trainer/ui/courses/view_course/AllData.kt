package com.kapilguru.trainer.ui.courses.view_course

import com.google.gson.annotations.SerializedName


data class AllData(
    @SerializedName("batches")
    val batches: List<BatchesItem>?,
    @SerializedName("showbatch")
    val showbatch: String = "",
    @SerializedName("course")
    val course: Course
)