package com.kapilguru.trainer.ui.guestLectures.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class GuestLectureData(
    @SerializedName("code") val code: String? = "",
    @SerializedName("lecture_date") val lectureDate: String? = "",
    @SerializedName("about") val about: String? = "",
    @SerializedName("video") val video: String? = "",
    @SerializedName("title") val title: String? = "",
    @SerializedName("duration") val duration: Int? = 0,
    @SerializedName("roomname") val roomName: String? = "",
    @SerializedName("no_of_attendees") val noOfAttendees: Int? = 0,
    @SerializedName("is_rejected") val isRejected: Int = 0,
    @SerializedName("registered_no_of_attendees") val registeredNoOfAttendees: Int? = 0,
    @SerializedName("videomeet_host_id") val videoMeetHostId: String? = "",
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("trainer_name") val trainerName: String? = "",
    @SerializedName("trainer_id") val trainerId: Int? = 0,
    @SerializedName("course_id") val courseId: Int? = 0,
    @SerializedName("image") val image: String? = "",
    @SerializedName("is_active") val isActive: Int? = 0,
    @SerializedName("languages") val languages: String? = "",
    @SerializedName("room_pass") val roomPass: String? = "",
    @SerializedName("modified_date") val modifiedDate: String? = "",
    @SerializedName("is_verified") val isVerified: Int = 0,
    @SerializedName("created_by") val createdBy: Int? = 0,
    @SerializedName("start_time") val startTime: String? = "",
    @SerializedName("verified_by") val verifiedBy: Int? = 0,
    @SerializedName("confrenceid") val conferenceId: Int? = 0,
    @SerializedName("meeting_url") val meetingUrl: String? = "",
    @SerializedName("modified_by") val modifiedBy: String? = "",
    @SerializedName("lecture_video") var lectureVideo: String? = "",
    @SerializedName("topic") val topic: String? = "",
    @SerializedName("created_date") val createdDate: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("db_ctime") val dbCtime: String? = "",
) : Serializable {
    var shouldShowChild = false
}