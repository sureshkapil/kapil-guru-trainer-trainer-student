package com.kapilguru.trainer.student.announcement.inbox.data

import com.google.gson.annotations.SerializedName

class StudentLastMessageRequest {
    @SerializedName("last_announcement_id")
     var lastAnnouncementId: Int?= -1
}