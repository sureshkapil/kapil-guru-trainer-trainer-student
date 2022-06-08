package com.kapilguru.trainer.announcement.newMessage.data

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class NewMessageData(
    @SerializedName("batch_id") var batchId: Int? = null,
    @SerializedName("course_id") var courseId: Int? = null,
    @SerializedName("course_title") var courseTitle: String? = "",
    @SerializedName("batch_code") var batchCode: String? = "",
    @SerializedName("trainer_id") var trainerId: String? = "",
    @SerializedName("trainer_name") var trainerName: String? = "",
    @SerializedName("batch_type") var batchType: String? = "",
    @SerializedName("start_date") var startDate: String? = "",
    @SerializedName("end_date") var endDate: String? = "",
    @SerializedName("max_no_of_students") var maxNoOfStudents: Int? = null,
    @SerializedName("current_no_of_students") var currentNoOfStudents: Int? = null,
    @SerializedName("percent_completion") var percentCompletion: Int? = null,
    @SerializedName("is_batch_filled") var isBatchFailed: Int? = null,
    @SerializedName("mon") var mon: Int? = null,
    @SerializedName("tue") var tue: Int? = null,
    @SerializedName("wed") var wed: Int? = null,
    @SerializedName("thu") var thu: Int? = null,
    @SerializedName("fri") var fri: Int? = null,
    @SerializedName("sat") var sat: Int? = null,
    @SerializedName("sun") var sun: Int? = null,
    @SerializedName("fee") var fee: Int? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("offer_price") var offer_price: Int? = null,
    @SerializedName("students_count") var studentsCount: Int? = null,
    @SerializedName("course_completion_count") var courseCompletionCount: Int? = null,
    @SerializedName("conducted_count") var conductedCount: Int? = null,
    @SerializedName("refunded_count") var refundedCount: Int? = null,
    @SerializedName("is_active") var isActive: Int? = null
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun toString(): String {
        val createdDate = getConvertedStartDate()
        val createdtime = getConvertedTime()
        return courseTitle?.toUpperCase(Locale.getDefault())+ "  |  " + batchCode.toString() + "  |  "+createdtime + "  |  " + createdDate
    }

    fun getConvertedStartDate(): String {
        val dateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        //You will get date object relative to server/client timezone wherever it is parsed
        val date: Date = dateFormat.parse(startDate)
        //If you need time just put specific format for time like 'HH:mm:ss'
        val formatter: DateFormat =
            SimpleDateFormat("MMM dd, yyyy")
        //If you need time just put specific format for time like 'HH:mm:ss'
        val dateStr: String? = formatter.format(date)
        Log.v("Check_info", dateStr!!)
        return dateStr
    }

    fun getConvertedTime(): String {
        val dateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        //You will get date object relative to server/client timezone wherever it is parsed
        val date: Date = dateFormat.parse(startDate)
        //If you need time just put specific format for time like 'HH:mm:ss'
        val formatter: DateFormat =
            SimpleDateFormat("hh:mm aa")
        //If you need time just put specific format for time like 'HH:mm:ss'
        val dateStr: String? = formatter.format(date)
        Log.v("Check_info", dateStr!!)
        return dateStr
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(batchId)
        parcel.writeValue(courseId)
        parcel.writeString(courseTitle)
        parcel.writeString(batchCode)
        parcel.writeString(trainerId)
        parcel.writeString(trainerName)
        parcel.writeString(batchType)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeValue(maxNoOfStudents)
        parcel.writeValue(currentNoOfStudents)
        parcel.writeValue(percentCompletion)
        parcel.writeValue(isBatchFailed)
        parcel.writeValue(mon)
        parcel.writeValue(tue)
        parcel.writeValue(wed)
        parcel.writeValue(thu)
        parcel.writeValue(fri)
        parcel.writeValue(sat)
        parcel.writeValue(sun)
        parcel.writeValue(fee)
        parcel.writeValue(duration)
        parcel.writeValue(offer_price)
        parcel.writeValue(studentsCount)
        parcel.writeValue(courseCompletionCount)
        parcel.writeValue(conductedCount)
        parcel.writeValue(refundedCount)
        parcel.writeValue(isActive)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewMessageData> {
        override fun createFromParcel(parcel: Parcel): NewMessageData {
            return NewMessageData(parcel)
        }

        override fun newArray(size: Int): Array<NewMessageData?> {
            return arrayOfNulls(size)
        }
    }

}
