package com.kapilguru.trainer.student.homeActivity.liveCourses.model

import com.google.gson.annotations.SerializedName

/*isRecoded - 0 means LiveCourse
* isRecorded - 1 means Recorded Courses
* isRecorded - 2 means StudentMaterial*/
data class LiveCourseResData(
    @SerializedName("doc_count") val docCount: Int = 0,
    @SerializedName("code") val code: String = "",
    @SerializedName("actual_fee") val actualFee: Double = 0.0,
    @SerializedName("course_title") val courseTitle: String = "",
    @SerializedName("fee") val fee: Int = 0,
    @SerializedName("duration_days") val durationDays: Int = 0,
    @SerializedName("video_count") val videoCount: Int = 0,
    @SerializedName("is_recorded") val isRecorded: Int = 0,
    @SerializedName("tp_count") val tpCount: Int = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("validity") val validity: String? = null,
    @SerializedName("course_image") val courseImage: String = "")