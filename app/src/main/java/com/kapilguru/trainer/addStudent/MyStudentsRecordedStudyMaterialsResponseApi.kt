package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class MyStudentsRecordedStudyMaterialsResponseApi(
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("study_material_id") val studyMaterialId: Int = 0,
    @SerializedName("student_id") val studentId: Int = 0,
    @SerializedName("course_completion") val courseCompletion: Int = 0,
    @SerializedName("is_recorded") val isRecorded: Int = 0,
    @SerializedName("fee_paid_id") val feePaidId: String = "",
    @SerializedName("student_code") val studentCode: String = "",
    @SerializedName("modified_date") val modifiedDate: String = "",
    @SerializedName("fee_paid_date") val feePaidDate: String = "",
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("fee_paid") val feePaid: Int = 0,
    @SerializedName("student_name") val studentName: String = "",
    @SerializedName("course_completion_date") val courseCompletionDate: String = "",
    @SerializedName("percent_completion") val percentCompletion: Int = 0,
    @SerializedName("modified_by") val modifiedBy: String = "",
    @SerializedName("course") val course: String = "",
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("trainer_id") val trainerId: Int = 0
)