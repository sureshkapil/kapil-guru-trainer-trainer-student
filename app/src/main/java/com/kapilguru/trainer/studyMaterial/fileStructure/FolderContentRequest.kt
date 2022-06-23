package com.kapilguru.trainer.studyMaterial.fileStructure

import com.google.gson.annotations.SerializedName

data class FolderContentRequest(
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("parent_id") var parentId: Int = 0,
    @SerializedName("study_material_id") var studyMaterialId: Int = 0
)