package com.kapilguru.trainer.allSubscription.positionSubscription.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import org.json.JSONObject
/*isPosition_No_occupied is set to true only  for objects whose value of isOwned is false. That is renewals will not be
   having is_position_no_occupied as true.*/
data class TrainerCourseData(
    @SerializedName("id") var id: Int = -1,
    @SerializedName("course_title") var courseTitle: String? = "",
    @SerializedName("trainer_id") var trainerId: Int = -1,
    @SerializedName("trainer_name") var trainerName: String? = "",
    var isOwned : Boolean = false,
    var ownedPosition : Int? = null,
    var isPosition_1_Occupied: Boolean = false,
    var isPosition_2_Occupied: Boolean = false,
    var isPosition_3_Occupied: Boolean = false,
    var isPosition_4_Occupied: Boolean = false,
    var isPosition_5_Occupied: Boolean = false,
    var positionToSubscribe : Int = -1,
    var myPosSubs : MyPositionData? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readParcelable(MyPositionData::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(courseTitle)
        parcel.writeInt(trainerId)
        parcel.writeString(trainerName)
        parcel.writeByte(if (isOwned) 1 else 0)
        parcel.writeValue(ownedPosition)
        parcel.writeByte(if (isPosition_1_Occupied) 1 else 0)
        parcel.writeByte(if (isPosition_2_Occupied) 1 else 0)
        parcel.writeByte(if (isPosition_3_Occupied) 1 else 0)
        parcel.writeByte(if (isPosition_4_Occupied) 1 else 0)
        parcel.writeByte(if (isPosition_5_Occupied) 1 else 0)
        parcel.writeInt(positionToSubscribe)
        parcel.writeParcelable(myPosSubs, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrainerCourseData> {
        override fun createFromParcel(parcel: Parcel): TrainerCourseData {
            return TrainerCourseData(parcel)
        }

        override fun newArray(size: Int): Array<TrainerCourseData?> {
            return arrayOfNulls(size)
        }
    }
}
