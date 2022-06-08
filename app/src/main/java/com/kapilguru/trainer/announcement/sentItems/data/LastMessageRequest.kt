package com.kapilguru.trainer.announcement.sentItems.data

import com.google.gson.annotations.SerializedName

class LastMessageRequest {
    @SerializedName("last_announcement_id")
     var lastAnnouncementId: Int?= -1
}