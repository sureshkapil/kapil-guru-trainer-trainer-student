package com.kapilguru.trainer.studyMaterial

import com.google.gson.annotations.SerializedName

data class StudyMaterialListResponseApi(
    @SerializedName("doc_count") var docCount: Int = 0,
    @SerializedName("actual_fee") var actualFee: Double = 0.0,
    @SerializedName("course_title") var courseTitle: String = "",
    @SerializedName("fee") var fee: Double = 0.0,
    @SerializedName("video_count") var videoCount: Int = 0,
    @SerializedName("is_recorded") var isRecorded: Int = 0,
    @SerializedName("id") var id: Int = 0
)