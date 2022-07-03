package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class OnlineStudentRequest(
    @SerializedName("email_id") var emailId: String? = "",
    @SerializedName("tenant_id") var tenantId: Int? = 0,
    @SerializedName("country_code") var countryCode: String? = "",
    @SerializedName("course_id") var courseId: String? = "",
    @SerializedName("batch_id") var batchId: Int? = 0,
    @SerializedName("name") var name: String? = "",
    @SerializedName("student_id") var studentId: Int? = 0,
    @SerializedName("is_other_country_code") var isOtherCountryCode: Int? = 0,
    @SerializedName("uuid") var uuid: String? = "",
    @SerializedName("contact_number") var contactNumber: String? = "",
    @SerializedName("is_added_by_trainer") var isAddedByTrainer: Int? = 0,
    @SerializedName("trainer_id") var trainerId: Int? = 0
)