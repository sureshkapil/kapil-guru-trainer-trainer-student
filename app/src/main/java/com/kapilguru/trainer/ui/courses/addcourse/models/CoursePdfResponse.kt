package com.kapilguru.trainer.ui.courses.addcourse.models

data class CoursePdfResponse(
    var data: List<CoursePdfResponseApi>,
    var message: String,
    var status: Int
)