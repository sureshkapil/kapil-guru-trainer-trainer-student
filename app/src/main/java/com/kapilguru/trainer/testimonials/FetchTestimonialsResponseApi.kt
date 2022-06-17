package com.kapilguru.trainer.testimonials

import com.google.gson.annotations.SerializedName

data class FetchTestimonialsResponseApi(
    @SerializedName("tenant_id") var tenantId: Int? = 0,
    @SerializedName("comments") var comments: String? = "",
    @SerializedName("is_active") var isActive: Int? = 0,
    @SerializedName("name") var name: String? = "",
    @SerializedName("modified_by") var modifiedBy: Int? = 0,
    @SerializedName("is_approved") var isApproved: Int? = 0,
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("created_date") var createdDate: String? = "",
    @SerializedName("modified_date") var modifiedDate: String? = null,
    @SerializedName("created_by") var createdBy: Int? = 0,
    @SerializedName("trainer_id") var trainerId: Int? = 0
)