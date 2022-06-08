package com.kapilguru.trainer.ui.courses.addcourse.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryApiData(
    @SerializedName("category_image")  val categoryImage: Any,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("created_by")  val createdBy: Any,
    @SerializedName("created_date") val createdDate: String,
    @SerializedName("id") val id: Int,
    @SerializedName("is_active") val isActive: Int,
    @SerializedName("modified_by") val modifiedBy: Any,
    @SerializedName("modified_date") val modifiedDate: String
)