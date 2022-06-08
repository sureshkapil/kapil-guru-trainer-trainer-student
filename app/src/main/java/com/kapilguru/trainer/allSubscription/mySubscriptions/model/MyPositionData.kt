package com.kapilguru.trainer.allSubscription.mySubscriptions.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class MyPositionData(
    var userId: Int = 0,
    var courseId: Int? = 0,
    var courseName: String? = "",
    var subsId: Int? = 0,
    var coursePositionId: Int? = 0,
    var coursePositionNum: Int? = 0,
    var subscriptionName: String? = null,
    var startDate: String? = null, //format : 2021-07-07 11:18:30
    var expiryDate: String? = null,
    var subscriptionAmount: Double? = 0.0
) : Parcelable {
    var shouldShowFooter = false

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
        shouldShowFooter = parcel.readByte() != 0.toByte()
    }

    fun parsePositiondata(myPosition: JSONObject): MyPositionData {
        if (myPosition.has("user_id"))
            this.userId = myPosition.getInt("user_id")
        if (myPosition.has("course_id"))
            this.courseId = myPosition.getInt("course_id")
        if (myPosition.has("course_name"))
            this.courseName = myPosition.getString("course_name")

        if (myPosition.has("subs_id"))
            this.subsId = myPosition.getInt("subs_id")

        if (myPosition.has("course_position_id"))
            this.coursePositionId = myPosition.getInt("course_position_id")

        if (myPosition.has("course_position_num"))
            this.coursePositionNum = myPosition.getInt("course_position_num")

        if (myPosition.has("subscription_name"))
            this.subscriptionName = myPosition.getString("subscription_name")

        if (myPosition.has("start_date"))
            this.startDate = myPosition.getString("start_date")

        if (myPosition.has("expiry_date"))
            this.expiryDate = myPosition.getString("expiry_date")
        if (myPosition.has("subscription_amount"))
            this.subscriptionAmount = myPosition.getDouble("subscription_amount")
        return this
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeValue(courseId)
        parcel.writeString(courseName)
        parcel.writeValue(subsId)
        parcel.writeValue(coursePositionId)
        parcel.writeValue(coursePositionNum)
        parcel.writeString(subscriptionName)
        parcel.writeString(startDate)
        parcel.writeString(expiryDate)
        parcel.writeValue(subscriptionAmount)
        parcel.writeByte(if (shouldShowFooter) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyPositionData> {
        override fun createFromParcel(parcel: Parcel): MyPositionData {
            return MyPositionData(parcel)
        }

        override fun newArray(size: Int): Array<MyPositionData?> {
            return arrayOfNulls(size)
        }
    }
}