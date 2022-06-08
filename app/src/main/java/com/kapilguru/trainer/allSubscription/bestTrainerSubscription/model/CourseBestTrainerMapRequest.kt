package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model

import com.google.gson.annotations.SerializedName

data class CourseBestTrainerMapRequest(
    @SerializedName("course_id") var courseId : Int,
    @SerializedName("course_badge_id") var courseBadgeId : Int,
    @SerializedName("expiry_date") var expiryDate : String,
    @SerializedName("start_date") var startDate : String,
    @SerializedName("trainer_id") var trainerId : Int)