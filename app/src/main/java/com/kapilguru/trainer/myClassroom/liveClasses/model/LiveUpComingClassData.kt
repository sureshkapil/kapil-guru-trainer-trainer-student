package com.kapilguru.trainer.myClassroom.liveClasses.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData

data class LiveUpComingClassData(
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("batch_code") val batchCode: String? = "",
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("batch_id") val batchId: Int = 0,
    @SerializedName("session_no") val sessionNo: Int = 0,
    @SerializedName("end_time") val endTime: String? = "",
    @SerializedName("is_conducted") val isConducted: Int = 0,
    @SerializedName("modified_date") val modifiedDate: String? = "",
    @SerializedName("created_by") val createdBy: String? = "",
    @SerializedName("db_ctime") val dbCtime: String? = "",
    @SerializedName("start_time") val startTime: String? = "",
    @SerializedName("batch_start_date") val batchStartDate: String? = "",
    @SerializedName("modified_by") val modifiedBy: String? = "",
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String? = "",
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("is_online") val isOnline: Int = 0,
    var batchData: NewMessageData? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(NewMessageData::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeString(batchCode)
        parcel.writeInt(isActive)
        parcel.writeInt(batchId)
        parcel.writeInt(sessionNo)
        parcel.writeString(endTime)
        parcel.writeInt(isConducted)
        parcel.writeString(modifiedDate)
        parcel.writeString(createdBy)
        parcel.writeString(dbCtime)
        parcel.writeString(startTime)
        parcel.writeString(batchStartDate)
        parcel.writeString(modifiedBy)
        parcel.writeInt(id)
        parcel.writeString(createdDate)
        parcel.writeInt(trainerId)
        parcel.writeInt(isOnline)
        parcel.writeParcelable(batchData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveUpComingClassData> {
        override fun createFromParcel(parcel: Parcel): LiveUpComingClassData {
            return LiveUpComingClassData(parcel)
        }

        override fun newArray(size: Int): Array<LiveUpComingClassData?> {
            return arrayOfNulls(size)
        }
    }
}