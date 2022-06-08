package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model

import com.google.gson.annotations.SerializedName

data class BestTrainerAllData(
    @SerializedName("badges") val bestTrainerBadges: String,
    @SerializedName("courses") val bestTrainerCourses: String)