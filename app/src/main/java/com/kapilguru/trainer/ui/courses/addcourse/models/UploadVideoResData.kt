package com.kapilguru.trainer.ui.courses.addcourse.models

import com.google.gson.annotations.SerializedName

data class UploadVideoResData(
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("originalname") val originalname: String = "",
    @SerializedName("modified_date") val modifiedDate: String? = null,
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("video_type") val videoType: String = "",
    @SerializedName("filename") val filename: String = "",
    @SerializedName("size") val size: Int = 0,
    @SerializedName("document_path") val documentPath: String = "",
    @SerializedName("modified_by") val modifiedBy: String? = null,
    @SerializedName("mimetype") val mimetype: String = "",
    @SerializedName("id") val id: Int = 0,
    @SerializedName("source_id") val sourceId: Int = 0,
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("insertId") val insertId: Int = 0)