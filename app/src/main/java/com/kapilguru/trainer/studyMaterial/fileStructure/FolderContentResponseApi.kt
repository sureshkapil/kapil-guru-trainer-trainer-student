package com.kapilguru.trainer.studyMaterial.fileStructure

import com.google.gson.annotations.SerializedName

data class FolderContentResponseApi(
    @SerializedName("course_id") var courseId: Int? = 0,
    @SerializedName("file_path") var filePath: String? = null,
    @SerializedName("filename") var filename: String? = null,
    @SerializedName("size") var size: Int? = 0,
    @SerializedName("originalname") var originalname: String? = null,
    @SerializedName("parent_id") var parentId: Int? = 0,
    @SerializedName("study_material_id") var studyMaterialId: Int? = 0,
    @SerializedName("name") var name: String? = null,
    @SerializedName("mimetype") var mimetype: String? =null,
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("trainer_id") var trainerId: Int? = 0,
    @SerializedName("is_folder") var isFolder: Int? = 0,
    @SerializedName("doc_count") var docCount: Int? = 0,
    @SerializedName("video_count") var videoCount: Int? = 0,
    @SerializedName("tp_count") var tpCount: Int? = 0,
)