package com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.model

import com.google.gson.annotations.SerializedName

data class GuestLectureStudentViewResData(
    @SerializedName("code") val code: String = "",
    @SerializedName("lecture_date") val lectureDate: String? = null,
    @SerializedName("about") val about: String = "",
    @SerializedName("video") val video: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("rejected_by") val rejectedBy: Int = 0,
    @SerializedName("duration") val duration: Int = 0,
    @SerializedName("roomname") val roomname: String = "",
    @SerializedName("rejected_reason") val rejectedReason: String? = null,
    @SerializedName("no_of_attendees") val noOfAttendees: Int = 0,
    @SerializedName("is_rejected") val isRejected: Int = 0,
    @SerializedName("registered_no_of_attendees") val registeredNoOfAttendees: Int = 0,
    @SerializedName("videomeet_host_id") val videomeetHostId: Int = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("trainer_name") val trainerName: String = "",
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("image") val image: String = "",
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("languages") val languages: String = "",
    @SerializedName("room_pass") val roomPass: String = "",
    @SerializedName("modified_date") val modifiedDate: String = "",
    @SerializedName("is_verified") val isVerified: Int = 0,
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("start_time") val startTime: String = "",
    @SerializedName("verified_by") val verifiedBy: Int = 0,
    @SerializedName("confrenceid") val confrenceid: Int = 0,
    @SerializedName("meeting_url") val meetingUrl: String? = null,
    @SerializedName("modified_by") val modifiedBy: String? = null,
    @SerializedName("lecture_video") var lectureVideo: String? = null,
    @SerializedName("topic") val topic: String = "",
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("status") val status: String = ""
)