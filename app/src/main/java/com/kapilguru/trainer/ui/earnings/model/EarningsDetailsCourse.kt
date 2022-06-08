package com.kapilguru.trainer.ui.earnings.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.apiDateFormatWithoutT
import org.json.JSONObject

data class EarningsDetailsCourse(
    @SerializedName("batch_id") var batchId: Int?=null,
    @SerializedName("batch_code") var batchCode: String?=null,
    @SerializedName("student_id") var studentId: Int?=null,
    @SerializedName("student_code") var studentCode: String?=null,
    @SerializedName("student_name") var studentName: String?=null,
    @SerializedName("enrollment_date") var enrollmentDate: String?=null,
    @SerializedName("total_fee") var totalFee: Double?=null,
    @SerializedName("start_date") var startDate: String?=null,
    @SerializedName("end_date") var endDate: String?=null
) : Parcelable{
    var shouldShowChild = false

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString()
    ) {
        shouldShowChild = parcel.readByte() != 0.toByte()
    }


    init {
        startDate = startDate.apiDateFormatWithoutT()
    }


    fun getEarningsCourseDetails(jsonstr: JSONObject): EarningsDetailsCourse {
       return  EarningsDetailsCourse().apply {
            batchId = jsonstr.getInt("batch_id")
            studentId = jsonstr.getInt("student_id")
            studentName = jsonstr.getString("student_name")
            batchCode = jsonstr.getString("batch_code")
            enrollmentDate = jsonstr.getString("enrollment_date")
            totalFee = jsonstr.getDouble("total_fee")
            startDate = jsonstr.getString("start_date")
            endDate = jsonstr.getString("end_date")
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(batchId)
        parcel.writeString(batchCode)
        parcel.writeValue(studentId)
        parcel.writeString(studentCode)
        parcel.writeString(studentName)
        parcel.writeString(enrollmentDate)
        parcel.writeValue(totalFee)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeByte(if (shouldShowChild) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EarningsDetailsCourse> {
        override fun createFromParcel(parcel: Parcel): EarningsDetailsCourse {
            return EarningsDetailsCourse(parcel)
        }

        override fun newArray(size: Int): Array<EarningsDetailsCourse?> {
            return arrayOfNulls(size)
        }
    }

}
