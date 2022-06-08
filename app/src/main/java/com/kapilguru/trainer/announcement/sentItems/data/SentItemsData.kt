package com.kapilguru.trainer.announcement.sentItems.data

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.convertBase64ToString

data class SentItemsData(
    @SerializedName("id") var id: Int?=0,
    @SerializedName("subject") var subject: String? = "",
    @SerializedName("message") var message: String? = "",
    @SerializedName("announcement_type") var announcementType: String?="",
    @SerializedName("sender_id") var senderId: Int?=0,
    @SerializedName("reciever_id") var recieverId: Int?=0,
    @SerializedName("created_by") var createdBy: Int?=0,
    @SerializedName("created_date") var createdDate: String?="",
    @SerializedName("modified_by") var modifiedBy: Int?=0,
    @SerializedName("modified_date") var modified_date: String?="",
    @SerializedName("is_active") var isActive: Int?=0,
    @SerializedName("sender_type") val senderType: String?="",
    @SerializedName("reciever_type") val recieverType: String?="",
    @SerializedName("reciever_name") val recieverName: String?="",
    @SerializedName("reciever_code") val recieverCode: String?="",
    @SerializedName("batch_code") val batchCode: String?="",
    @SerializedName("batch_time") val batchTime: String?="",
    var previousTappedPosition: Int? = null,
    var shouldShow: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
): Parcelable {
    val TAG = "SentItemsData"

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    fun getDecodeSubject(): String {
        val data = String.convertBase64ToString(subject)
        return data
    }

    fun getDecodeMessage(): String {
        val data = String.convertBase64ToString(message)
        return data
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(subject)
        parcel.writeString(message)
        parcel.writeString(announcementType)
        parcel.writeValue(senderId)
        parcel.writeValue(recieverId)
        parcel.writeValue(createdBy)
        parcel.writeString(createdDate)
        parcel.writeValue(modifiedBy)
        parcel.writeString(modified_date)
        parcel.writeValue(isActive)
        parcel.writeString(senderType)
        parcel.writeString(recieverType)
        parcel.writeString(recieverName)
        parcel.writeString(recieverCode)
        parcel.writeString(batchCode)
        parcel.writeString(batchTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SentItemsData> {
        override fun createFromParcel(parcel: Parcel): SentItemsData {
            return SentItemsData(parcel)
        }

        override fun newArray(size: Int): Array<SentItemsData?> {
            return arrayOfNulls(size)
        }
    }
}