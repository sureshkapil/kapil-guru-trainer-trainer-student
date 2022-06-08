package com.kapilguru.trainer.batchExamReports

import com.google.gson.annotations.SerializedName

data class BatchExamResultModel(
    @SerializedName("low_score")
    val lowScore: Int = 0,
    @SerializedName("appeared_students")
    val appearedStudents: Int = 0,
    @SerializedName("total_marks")
    val totalMarks: Int = 0,
    @SerializedName("total_students")
    val totalStudents: Int = 0,
    @SerializedName("high_score")
    val highScore: Int = 0,
    @SerializedName("avg_time")
    val avgTime: Int = 0
)