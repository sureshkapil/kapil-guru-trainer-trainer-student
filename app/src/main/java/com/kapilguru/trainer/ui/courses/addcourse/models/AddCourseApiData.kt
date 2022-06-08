package com.kapilguru.trainer.ui.courses.addcourse.models

import androidx.annotation.Keep

@Keep
data class AddCourseApiData(
    val insertId: Int,
    var code: String = ""
)