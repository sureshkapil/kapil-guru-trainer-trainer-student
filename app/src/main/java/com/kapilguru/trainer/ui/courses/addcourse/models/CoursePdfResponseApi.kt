package com.kapilguru.trainer.ui.courses.addcourse.models

import com.google.gson.annotations.SerializedName

data class CoursePdfResponseApi(
    @SerializedName("document_title")
    var documentTitle: String? = "",
    @SerializedName("course_id")
    var courseId: Int? = 0,
    @SerializedName("is_active")
    var isActive: Int? = 0,
    @SerializedName("originalname")
    var originalname: String? = "",
    @SerializedName("modified_date")
    var modifiedDate: String? = null,
    @SerializedName("created_by")
    var createdBy: Int? = 0,
    @SerializedName("filename")
    var filename: String? = "",
    @SerializedName("size")
    var size: Int? = 0,
    @SerializedName("document_path")
    var documentPath: String? = "",
    @SerializedName("modified_by")
    var modifiedBy: String? = null,
    @SerializedName("mimetype")
    var mimetype: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("created_date")
    var createdDate: String = "",
    @SerializedName("trainer_id")
    var trainerId: Int = 0
)