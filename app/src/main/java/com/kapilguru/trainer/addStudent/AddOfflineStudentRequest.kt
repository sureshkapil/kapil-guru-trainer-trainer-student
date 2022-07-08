package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class AddOfflineStudentRequest(
    @SerializedName("tenant_id") var tenantId: Int? = 0,
    @SerializedName("email_id") var emailId: String? = "",
    @SerializedName("days_attended") var daysAttended: Int? = 0,
    @SerializedName("fee") var fee: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("course") var course: String? = "",
    @SerializedName("days_conducted") var daysConducted: Int? = 0,
    @SerializedName("batch_date") var batchDate: String? = "",
    @SerializedName("contact_number") var contactNumber: String? = "",
    @SerializedName("trainer_id") var trainerId: Int? = 0,
    @SerializedName("status") var status: String? = ""
)