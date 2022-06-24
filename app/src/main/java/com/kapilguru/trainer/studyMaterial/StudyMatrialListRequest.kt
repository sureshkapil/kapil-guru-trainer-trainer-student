package com.kapilguru.trainer.studyMaterial

import com.google.gson.annotations.SerializedName

data class StudyMatrialListRequest(@SerializedName("is_recorded")
                                   var isRecorded: Int? = 0,
                                   @SerializedName("trainer_id")
                                   var trainerId: Int? = 0)