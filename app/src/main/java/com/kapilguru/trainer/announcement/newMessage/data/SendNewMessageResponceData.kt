package com.kapilguru.trainer.announcement.newMessage.data

import com.google.gson.annotations.SerializedName

class SendNewMessageResponceData(
@SerializedName("fieldCount") var fieldCount :Int,
@SerializedName("affectedRows") var affectedRows :Int,
@SerializedName("insertId") var insertId :Int,
@SerializedName("serverStatus") var serverStatus :Int,
@SerializedName("warningCount") var warningCount :Int,
@SerializedName("message") var message :String = "",
@SerializedName("protocol41") var protocol41 :Boolean,
@SerializedName("changedRows") var changedRows :Int
)