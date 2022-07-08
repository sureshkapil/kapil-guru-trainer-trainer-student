package com.kapilguru.trainer.coupons

import com.google.gson.annotations.SerializedName

data class StudentRequestModel(@SerializedName("tenant_id")
                               var tenantId: Int? = 0,
                               @SerializedName("trainer_id")
                               var trainerId: Int? = 0)