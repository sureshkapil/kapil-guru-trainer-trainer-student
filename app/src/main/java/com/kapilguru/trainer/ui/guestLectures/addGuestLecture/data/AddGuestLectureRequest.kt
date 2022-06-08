package com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data

import android.text.TextUtils
import android.util.Log
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.SENT_FOR_APPROVAL
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import com.kapilguru.trainer.fromBase64
import com.kapilguru.trainer.fromBase64ToString


data class AddGuestLectureRequest(
    @SerializedName("trainer_id") var trainerId : Int? = null,
    @SerializedName("course_id") var courseId : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("about") var about : String? = null,
    @SerializedName("duration") var duration : Int? = null,
    @SerializedName("languages") var languages : String? = null,
    @SerializedName("trainer_name") var trainerName : String? = null,
    @SerializedName("lecture_date") var lectureDate : String? = null,
    @SerializedName("lecture_video") var lectureVideo : String? = null,
    @SerializedName("code") var code : String? = null,
//    @SerializedName("start_time") var startTime : String? = null,
    @Transient var startTime : String? = null,
    @SerializedName("no_of_attendees") var noOfAttendees : Int? = null,
    @SerializedName("status") var status : String? = SENT_FOR_APPROVAL,
//    @SerializedName("image") var image : String? = "",
    @Transient var image : String? = "",
//    @SerializedName("lecture_video") var video : String? = "",
    @Transient var video : String? = "",
    @SerializedName("is_verified") var isVerified : Int = 0,
    @SerializedName("is_rejected") var isRejected: Int = 0,
    @Transient var isApiRequestMade: Boolean = false,
    // id is used for update(i.e, after adding details and uploading image and video) and edit(editing details, image and video).
    @Transient var id : Int = -1){

    fun getAddGuestLectureReqFromGuestLectureData(guestLectureData: GuestLectureData) : AddGuestLectureRequest{
        this.trainerId = guestLectureData.trainerId
        this.courseId = guestLectureData.courseId
        this.title = guestLectureData.title
        this.image = guestLectureData.image
        this.video = guestLectureData.video
        if(TextUtils.isEmpty(guestLectureData.about)){
            this.about = ""
        }else{
            this.about = guestLectureData.about?.fromBase64()
        }
        this.duration = guestLectureData.duration
        this.languages = guestLectureData.languages?.fromBase64()
        this.trainerName = guestLectureData.trainerName
        this.lectureDate = guestLectureData.lectureDate
        this.lectureDate = getConvertedLectureDate()
        this.startTime = getConvertedStartTime(guestLectureData.lectureDate!!)
        this.noOfAttendees = guestLectureData.noOfAttendees
        this.status = guestLectureData.status
        this.id = guestLectureData.id!!
        this.lectureVideo = guestLectureData.lectureVideo
        this.code = guestLectureData.code
        return this
    }

    fun getConvertedLectureDate(): String {
        val dateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        //You will get date object relative to server/client timezone wherever it is parsed
        val date: Date = dateFormat.parse(lectureDate)
        //If you need time just put specific
        // for time like 'HH:mm:ss'
        val formatter: DateFormat =
            SimpleDateFormat("MM/dd/yy")
        //If you need time just put specific format for time like 'HH:mm:ss'
        val dateStr: String? = formatter.format(date)
        Log.v("Check_info", dateStr!!)
        return dateStr
    }

    fun getConvertedStartTime(lectureDateAndTime : String): String {
        val dateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        //You will get date object relative to server/client timezone wherever it is parsed
        val date: Date = dateFormat.parse(lectureDateAndTime)
        //If you need time just put specific
        // for time like 'HH:mm:ss'
        val formatter: DateFormat =
            SimpleDateFormat("hh:mm aa")
        //If you need time just put specific format for time like 'HH:mm:ss'
        val dateStr: String? = formatter.format(date)
        Log.v("Check_info", dateStr!!)
        return dateStr
    }
}
