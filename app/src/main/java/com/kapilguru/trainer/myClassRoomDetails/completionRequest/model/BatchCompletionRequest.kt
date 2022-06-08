package com.kapilguru.trainer.myClassRoomDetails.completionRequest.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BatchCompletionRequest(
    @SerializedName("batch_id") var batchId: Int = -1,
    @SerializedName("course_id") var courseId: Int = -1,
    @SerializedName("trainer_id") var trainerId: Int = -1
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(batchId)
        parcel.writeInt(courseId)
        parcel.writeInt(trainerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BatchCompletionRequest> {
        override fun createFromParcel(parcel: Parcel): BatchCompletionRequest {
            return BatchCompletionRequest(parcel)
        }

        override fun newArray(size: Int): Array<BatchCompletionRequest?> {
            return arrayOfNulls(size)
        }
    }
}