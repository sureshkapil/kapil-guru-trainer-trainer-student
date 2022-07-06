package com.kapilguru.trainer.student.announcement.newMessage.data

import com.google.gson.annotations.SerializedName

data class StudentAdminMessageData(
    @SerializedName("user_id") var userId : Int? = null,
    @SerializedName("code") var code : String? = null,
    @SerializedName("name") var name : String? = null,
){
    override fun toString(): String {
        return code.toString()+"  |  "+(name.toString())
    }
}