package com.kapilguru.trainer.myClassRoomDetails.studymaterial.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class BatchDocumentData(
    @SerializedName("batch_id")
    val batchId: Int?,
    @SerializedName("course_id")
    val courseId: Int?,
    @SerializedName("created_by")
    val createdBy: Any?,
    @SerializedName("created_date")
    val createdDate: String?,
    @SerializedName("document_path")
    val documentPath: String?,
    @SerializedName("document_title")
    val documentTitle: String?,
    @SerializedName("filename")
    val filename: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_active")
    val isActive: Int?,
    @SerializedName("mimetype")
    val mimetype: String?,
    @SerializedName("modified_by")
    val modifiedBy: Any?,
    @SerializedName("modified_date")
    val modifiedDate: Any?,
    @SerializedName("originalname")
    val originalname: String?,
    @SerializedName("size")
    val size: Int?,
    @SerializedName("trainer_id")
    val trainerId: Int?
)