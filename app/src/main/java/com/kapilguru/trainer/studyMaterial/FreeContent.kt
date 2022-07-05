package com.kapilguru.trainer.studyMaterial

import com.google.gson.annotations.SerializedName

data class FreeContent(
    @SerializedName("course_id") var courseId: Int? = 0,
    @SerializedName("file_path") var filePath: String? = "",
    @SerializedName("is_active") var isActive: Int? = 0,
    @SerializedName("originalname") var originalname: String? = "",
    @SerializedName("study_material_id") var studyMaterialId: Int? = 0,
    @SerializedName("type") var type: String? = "",
    @SerializedName("title") var title: String? = "",
    @SerializedName("modified_date") var modifiedDate: String? = "",
    @SerializedName("created_by") var createdBy: String? = null,
    @SerializedName("filename") var filename: String? = "",
    @SerializedName("size") var size: Int? = 0,
    @SerializedName("parent_id") var parentId: Int? = 0,
    @SerializedName("is_free") var isFree: Int? = 0,
    @SerializedName("modified_by") var modifiedBy: Int? = null,
    @SerializedName("mimetype") var mimetype: String? = "",
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("created_date") var createdDate: String? = "",
    @SerializedName("trainer_id") var trainerId: Int? = 0
)