package com.kapilguru.trainer.ui.guestLectures.guestLectureDetails.viewModel

import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureData

class GuestLectureDetailsViewModel : ViewModel() {
    private val TAG = "DemoLectureDetailsViewModel"

    var guestLectureDetails: GuestLectureData = GuestLectureData()
}