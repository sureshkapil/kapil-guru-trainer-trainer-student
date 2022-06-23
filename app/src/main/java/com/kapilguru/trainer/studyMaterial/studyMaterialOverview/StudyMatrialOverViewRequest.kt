package com.kapilguru.trainer.studyMaterial.studyMaterialOverview

import com.google.gson.annotations.SerializedName

data class StudyMatrialOverViewRequest(
    @SerializedName("course_id") var courseId: Int? = 0, @SerializedName("trainer_id") var trainerId: Int? = 0
)