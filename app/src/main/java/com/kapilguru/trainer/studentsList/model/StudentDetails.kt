package com.kapilguru.trainer.studentsList.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Keep
data class StudentDetails(
    @SerializedName("batch_id") var batchId: Int = 0,
    @SerializedName("attended_count") var attendedCount: Int = 0,
    @SerializedName("sessions_count") var sessionsCount: Int = 0,
    @SerializedName("conducted_count") var conductedCount: Int = 0,
    @SerializedName("batch_code") var batchCode: String? = null,
    @SerializedName("course_completion") var courseCompletion: Int = 0,
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("course_title") var courseTitle: String?="",
    @SerializedName("percent_completion") var percentCompletion: Int? = 0,
    @SerializedName("start_date") var startDate: String? = null,
    @SerializedName("start_time") var startTime: String? = null,
    @SerializedName("student_id") var studentId: Int = 0,
    @SerializedName("student_code") var studentCode: String? = null,
    @SerializedName("student_name") var studentName: String? = null,
    @SerializedName("student_rating") var studentRating: Int = 0,
    var previousTappedPosition: Int? = null,
    val shouldShow: MutableLiveData<Boolean> = MutableLiveData(false)
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as Int
//        parcel.readValue(Int::class.java.classLoader) as? Int

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(batchId)
        parcel.writeValue(attendedCount)
        parcel.writeValue(sessionsCount)
        parcel.writeValue(conductedCount)
        parcel.writeString(batchCode)
        parcel.writeValue(courseCompletion)
        parcel.writeValue(courseId)
        parcel.writeString(courseTitle)
        parcel.writeValue(percentCompletion)
        parcel.writeString(startDate)
        parcel.writeString(startTime)
        parcel.writeValue(studentId)
        parcel.writeString(studentCode)
        parcel.writeString(studentName)
        parcel.writeValue(studentRating)
//        parcel.writeValue(previousTappedPosition)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentDetails> {

        override fun createFromParcel(parcel: Parcel): StudentDetails {
            return StudentDetails(parcel)
        }

        override fun newArray(size: Int): Array<StudentDetails?> {
            return arrayOfNulls(size)
        }
    }

    var getBatchId = batchId.toString()
    var getcourseCompletion = courseCompletion.toString()
    var getcourseId = courseId.toString()
    var getpercentCompletion = percentCompletion.toString()
    var getstudentId = studentId.toString()
    var getstudentRating = studentRating.toString()

    fun getConvertedStartDate(): String {
        val dateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        //You will get date object relative to server/client timezone wherever it is parsed
        val date: Date = dateFormat.parse(startDate)
        //If you need time just put specific format for time like 'HH:mm:ss'
        val formatter: DateFormat =
            SimpleDateFormat("yyyy-MM-dd")
        //If you need time just put specific format for time like 'HH:mm:ss'
        val dateStr: String? = formatter.format(date)
        Log.v("Check_info", dateStr!!)
        return dateStr
    }

}
