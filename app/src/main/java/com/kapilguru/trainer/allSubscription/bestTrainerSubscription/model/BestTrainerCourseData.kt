package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData

data class BestTrainerCourseData(
    @SerializedName("id") var id: Int,
    @SerializedName("course_title") var courseTitle: String? = "",
    @SerializedName("trainer_id") var trainerId: Int,
    @SerializedName("trainer_name") var trainerName: String? = ""
) : Parcelable {
    var isBadgeBought = false
    var expiryDate: String? = ""
    var myBestTrainerData: MyBestTrainerData? = null

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
        isBadgeBought = parcel.readByte() != 0.toByte()
        expiryDate = parcel.readString()
        myBestTrainerData = parcel.readParcelable(MyBestTrainerData::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(courseTitle)
        parcel.writeInt(trainerId)
        parcel.writeString(trainerName)
        parcel.writeByte(if (isBadgeBought) 1 else 0)
        parcel.writeString(expiryDate)
        parcel.writeParcelable(myBestTrainerData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BestTrainerCourseData> {
        override fun createFromParcel(parcel: Parcel): BestTrainerCourseData {
            return BestTrainerCourseData(parcel)
        }

        override fun newArray(size: Int): Array<BestTrainerCourseData?> {
            return arrayOfNulls(size)
        }
    }
}
