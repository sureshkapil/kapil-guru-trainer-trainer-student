package com.kapilguru.trainer.faculty

import com.google.gson.annotations.SerializedName

data class GetFacultyRequest(
    @SerializedName("tenant_id") var tenantId: Int? = 0, @SerializedName("trainer_id") var trainerId: Int? = 0
)