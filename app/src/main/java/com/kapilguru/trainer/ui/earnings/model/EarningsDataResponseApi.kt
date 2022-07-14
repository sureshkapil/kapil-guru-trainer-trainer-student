package com.kapilguru.trainer.ui.earnings.model

import com.google.gson.annotations.SerializedName

data class EarningsDataResponseApi(
    @SerializedName("recorded_courses") var recordedCourses: Double = 0.0,
    @SerializedName("live_courses") var liveCourses: Double = 0.0,
    @SerializedName("total_earnings") var totalEarnings: Double = 0.0,
    @SerializedName("study_materials") var studyMaterials: Double = 0.0
)