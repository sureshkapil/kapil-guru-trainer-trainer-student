package com.kapilguru.trainer.faculty

import com.google.gson.annotations.SerializedName

data class FacultyListResponseApi(
    @SerializedName("tenant_id") val tenantId: Int = 0,
    @SerializedName("create_coupons") val createCoupons: Int = 0,
    @SerializedName("email_id") val emailId: String = "",
    @SerializedName("add_recording_course") val addRecordingCourse: Int = 0,
    @SerializedName("subscriptions") val subscriptions: Int = 0,
    @SerializedName("my_students") val myStudents: Int = 0,
    @SerializedName("add_study_materials") val addStudyMaterials: Int = 0,
    @SerializedName("my_enquiries") val myEnquiries: Int = 0,
    @SerializedName("social_media_links") val socialMediaLinks: Int = 0,
    @SerializedName("attendance_management") val attendanceManagement: Int = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("gallery") val gallery: Int = 0,
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("add_live_course") val addLiveCourse: Int = 0,
    @SerializedName("add_exams") val addExams: Int = 0,
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("today_follow_ups") val todayFollowUps: Int = 0,
    @SerializedName("fee_management") val feeManagement: Int = 0,
    @SerializedName("modified_date") val modifiedDate: String = "",
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("contact_number") val contactNumber: String = "",
    @SerializedName("promotional_images") val promotionalImages: Int = 0,
    @SerializedName("testimonials") val testimonials: Int = 0,
    @SerializedName("earnings") val earnings: Int = 0,
    @SerializedName("certificates") val certificates: Int = 0,
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("add_free_classes") val addFreeClasses: Int = 0,
    @SerializedName("modified_by") val modifiedBy: String? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("messages") val messages: Int = 0,
    @SerializedName("created_date") val createdDate: String = ""
)