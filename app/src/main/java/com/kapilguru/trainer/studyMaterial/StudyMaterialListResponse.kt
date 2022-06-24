package com.kapilguru.trainer.studyMaterial

import com.google.gson.annotations.SerializedName

data class StudyMaterialListResponse(
    @SerializedName("data") var studyMaterialListResponseApi: List<StudyMaterialListResponseApi>?, @SerializedName("message") var message: String? = "", @SerializedName("status") var status: Int? = 0
)