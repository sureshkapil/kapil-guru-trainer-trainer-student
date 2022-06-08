package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BestTrainerBadgeData(
    @SerializedName("id") var id : Int,
    @SerializedName("badge_id") var badgeId : Int,
    @SerializedName("course_id") var courseId : Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(badgeId)
        parcel.writeInt(courseId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BestTrainerBadgeData> {
        override fun createFromParcel(parcel: Parcel): BestTrainerBadgeData {
            return BestTrainerBadgeData(parcel)
        }

        override fun newArray(size: Int): Array<BestTrainerBadgeData?> {
            return arrayOfNulls(size)
        }
    }
}