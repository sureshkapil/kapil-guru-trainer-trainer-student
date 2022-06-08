package com.kapilguru.trainer.batchExamReports

import com.google.gson.annotations.SerializedName

data class BatchExamStudents(
    @SerializedName("exam_students")
    val batchStudentsItem: List<BatchStudentsItem>?
)