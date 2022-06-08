package com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureData

 class AddGuestLectureResData(
    @SerializedName("insertId") val insertId: Int = 0
) : GuestLectureData()