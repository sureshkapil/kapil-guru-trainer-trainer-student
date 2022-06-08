package com.kapilguru.trainer.allSubscription.mySubscriptions.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class MyBestTrainerData(
    var userId : Int? = 0,
    var courseId : Int  =0,
    var courseName : String? = null,
    var subsId : Int? = 0,
    var courseBadgeId : Int? = 0,
    var subscriptionName  :String? = null,
    var startDate : String? = null,
    var expiryDate : String? = null,
    var subscriptionAmount : Double? = 0.0
) : Parcelable{
    var shouldShowFooter = false

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
        shouldShowFooter = parcel.readByte() != 0.toByte()
    }

    fun parseBestTrainer(bestTrainer : JSONObject) : MyBestTrainerData{
        if(bestTrainer.has("user_id"))
            this.userId = bestTrainer.getInt("user_id")
        if(bestTrainer.has("course_id"))
            this.courseId = bestTrainer.getInt("course_id")
        if(bestTrainer.has("course_name"))
            this.courseName = bestTrainer.getString("course_name")
        if(bestTrainer.has("course_badge_id"))
            this.courseBadgeId = bestTrainer.getInt("course_badge_id")
        if(bestTrainer.has("subscription_name"))
            this.subscriptionName = bestTrainer.getString("subscription_name")
        if(bestTrainer.has("start_date"))
            this.startDate = bestTrainer.getString("start_date")
        if(bestTrainer.has("expiry_date"))
            this.expiryDate = bestTrainer.getString("expiry_date")
        if(bestTrainer.has("subscription_amount"))
            this.subscriptionAmount = bestTrainer.getDouble("subscription_amount")
        return this
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(userId)
        parcel.writeInt(courseId)
        parcel.writeString(courseName)
        parcel.writeValue(subsId)
        parcel.writeValue(courseBadgeId)
        parcel.writeString(subscriptionName)
        parcel.writeString(startDate)
        parcel.writeString(expiryDate)
        parcel.writeValue(subscriptionAmount)
        parcel.writeByte(if (shouldShowFooter) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyBestTrainerData> {
        override fun createFromParcel(parcel: Parcel): MyBestTrainerData {
            return MyBestTrainerData(parcel)
        }

        override fun newArray(size: Int): Array<MyBestTrainerData?> {
            return arrayOfNulls(size)
        }
    }
}