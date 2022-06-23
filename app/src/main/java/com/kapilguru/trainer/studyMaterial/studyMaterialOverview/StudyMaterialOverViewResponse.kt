package com.kapilguru.trainer.studyMaterial.studyMaterialOverview

import com.google.gson.annotations.SerializedName

data class StudyMaterialOverViewResponse(
    @SerializedName("data") var studyMaterialOverViewResponseApi: List<StudyMaterialOverViewResponseApi>?,
    @SerializedName("message") var message: String = "",
    @SerializedName("status") var status: Int = 0
)