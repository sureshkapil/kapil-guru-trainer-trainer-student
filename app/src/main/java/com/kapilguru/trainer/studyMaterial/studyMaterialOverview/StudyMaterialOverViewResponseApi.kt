package com.kapilguru.trainer.studyMaterial.studyMaterialOverview

import com.google.gson.annotations.SerializedName

data class StudyMaterialOverViewResponseApi(
    @SerializedName("doc_count") var docCount: Int? = 0,
    @SerializedName("actual_fee") var actualFee: Double? = 0.0,
    @SerializedName("course_title") var courseTitle: String? = "",
    @SerializedName("fee") var fee: Double? = 0.0,
    @SerializedName("video_count") var videoCount: Int? = 0,
    @SerializedName("is_recorded") var isRecorded: Int? = 0,
    @SerializedName("description") var description: String? = "",
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("category") var category: String? = "",
    @SerializedName("course_image") var courseImage: String? = null,
    @SerializedName("study_material_id") var studyMaterialId: Int? = 0
)