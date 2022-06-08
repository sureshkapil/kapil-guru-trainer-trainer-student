package com.kapilguru.trainer.ui.courses.add_batch.models

data class AddBatchApiData(
    val affectedRows: Int,
    val changedRows: Int,
    val fieldCount: Int,
    val insertId: Int,
    val message: String,
    val protocol41: Boolean,
    val serverStatus: Int,
    val warningCount: Int
)