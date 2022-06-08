package com.kapilguru.trainer.announcement.newMessage.data

import com.google.gson.annotations.SerializedName

/*Same class is used to read daat from api (admin list) and
* to end selected admin in new message.*/
data class AdminMessageData(
    @SerializedName("user_id") var userId: Int?,
    @SerializedName("name") var name: String?
){
    override fun toString(): String {
        return name.toString()
    }
}