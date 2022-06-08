package com.kapilguru.trainer.ui.courses.batchesList.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class BatchListApiRequest(
    @SerializedName("trainer_id") val trainerId : Int,
    @SerializedName("course_id") val courseId : String
)