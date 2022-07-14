package com.kapilguru.trainer.ui.earnings.earningsDetails

import com.google.gson.annotations.SerializedName

data class EarningsDetailsResponseApi(
    @SerializedName("fee_paid") var feePaid: Double? = 0.0,
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("study_material_id") var studyMaterialId: Double? = 0.0,
    @SerializedName("name") var name: String? = null,
    @SerializedName("course") var course: String? = null,
    @SerializedName("student_id") var studentId: Int = 0,
    @SerializedName("student_code") var studentCode: String = "",
    @SerializedName("fee_paid_id") var feePaidId: String = "",
    @SerializedName("batch_code") var batchCode: String? = null,
    @SerializedName("fee_paid_date") var feePaidDate: String? = null,
    @SerializedName("trainer_id") var trainerId: Int = 0,
    @SerializedName("start_date") var startDate: String? = null
)