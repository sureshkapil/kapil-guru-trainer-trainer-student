package com.kapilguru.trainer.ui.webiner.model

import com.google.gson.annotations.SerializedName

data class LiveUpComingWebinarData(
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("speaker_name") val speakerName: String = "",
    @SerializedName("session_no") val sessionNo: Int = 0,
    @SerializedName("end_time") val endTime: String = "",
    @SerializedName("is_conducted") val isConducted: Int = 0,
    @SerializedName("modified_date") val modifiedDate: String? = null,
    @SerializedName("created_by") val createdBy: Int? = null,
    @SerializedName("webinar_code") val webinarCode: String = "",
    @SerializedName("db_ctime") val dbCtime: String = "",
    @SerializedName("start_time") val startTime: String = "",
    @SerializedName("webinar_id") val webinarId: Int = 0,
    @SerializedName("meeting_url") val meetingUrl: String? = null,
    @SerializedName("modified_by") val modifiedBy: Int? = null,
    @SerializedName("webinar_title") val webinarTitle: String = "",
    @SerializedName("registered_no_of_attendees") val registeredNoOfAttendees: Int = 0,
    @SerializedName("videomeet_host_id") val videoMeetHostId: Int = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("trainer_id") val trainerId: Int = 0)