package com.kapilguru.trainer.allSubscription.positionSubscription.model

import com.google.gson.annotations.SerializedName

data class CoursePositionMapResData(
    @SerializedName("affectedRows") var affectedRows : Int,
    @SerializedName("changedRows") var changedRows : Int,
    @SerializedName("fieldCount") var fieldCount : Int,
    @SerializedName("insertId") var insertId : Int,
    @SerializedName("message") var message : String,
    @SerializedName("protocol41") var protocol41 : Boolean,
    @SerializedName("serverStatus") var serverStatus : Int)

