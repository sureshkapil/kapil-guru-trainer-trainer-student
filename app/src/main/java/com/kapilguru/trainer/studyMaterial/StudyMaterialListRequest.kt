package com.kapilguru.trainer.studyMaterial

import com.google.gson.annotations.SerializedName

data class StudyMaterialListRequest(@SerializedName("is_recorded")
                                    var isRecorded: String? = "",
                                    @SerializedName("trainer_id")
                                    var trainerId: Int? = 0)