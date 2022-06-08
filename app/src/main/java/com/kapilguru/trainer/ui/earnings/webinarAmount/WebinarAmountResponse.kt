package com.kapilguru.trainer.ui.earnings.webinarAmount

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class WebinarAmountResponse(
    @SerializedName("user_id")
    var userId: Int? = 0,
    @SerializedName("webinar_id")
    var webinarId: Int? = 0,
    @SerializedName("attendee_code")
    var attendeeCode: String? = "",
    @SerializedName("fee")
    var fee: Double? = 0.0,
    @SerializedName("attendee_id")
    var attendeeId: Int? = 0,
    @SerializedName("attendee_name")
    var attendeeName: String? = "",
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("enrollment_date")
    var enrollmentDate: String? = "",
    @SerializedName("start_date")
    var startDate: String? = "" 
):Parcelable {

   init {
//       startDate = startDate.apiDateFormatWithoutT()
   }

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun getWebinarAmountDetails(jsonstr: JSONObject): WebinarAmountResponse {
            return WebinarAmountResponse().apply {
                userId = jsonstr.getInt("user_id")
                webinarId = jsonstr.getInt("webinar_id")
                attendeeCode = jsonstr.getString("attendee_code")
                fee = jsonstr.getDouble("fee")
                attendeeId = jsonstr.getInt("attendee_id")
                attendeeName = jsonstr.getString("attendee_name")
                title = jsonstr.getString("title")
                enrollmentDate = jsonstr.getString("enrollment_date")
                startDate = jsonstr.getString("start_date")
            }
        }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(userId)
        parcel.writeValue(webinarId)
        parcel.writeString(attendeeCode)
        parcel.writeValue(fee)
        parcel.writeValue(attendeeId)
        parcel.writeString(attendeeName)
        parcel.writeString(title)
        parcel.writeString(enrollmentDate)
        parcel.writeString(startDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WebinarAmountResponse> {
        override fun createFromParcel(parcel: Parcel): WebinarAmountResponse {
            return WebinarAmountResponse(parcel)
        }

        override fun newArray(size: Int): Array<WebinarAmountResponse?> {
            return arrayOfNulls(size)
        }
    }
}