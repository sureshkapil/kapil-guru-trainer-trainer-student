package com.kapilguru.trainer.studyMaterial.fileStructure

import com.google.gson.annotations.SerializedName

data class FolderContentResponse(
    @SerializedName("data") val folderContentResponseApi: List<FolderContentResponseApi>?, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)