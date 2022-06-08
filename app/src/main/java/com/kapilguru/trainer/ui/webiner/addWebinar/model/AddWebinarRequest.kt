package com.kapilguru.trainer.ui.webiner.addWebinar.model

import android.widget.EditText
import androidx.databinding.InverseBindingAdapter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.SENT_FOR_APPROVAL

data class AddWebinarRequest(
    @Expose @SerializedName("trainer_id") var trainerID: String? = null,
    @Expose @SerializedName("title") var title: String? = null,
    @Expose @SerializedName("image") var image: String? = null,
    @Expose @SerializedName("webinar_video") var video: String? = null,
    @Expose @SerializedName("about") var about: String? = null,
    @Expose @SerializedName("duration_days") var durationDays: String? = null,
    @Expose @SerializedName("duration_hrs") var durationHrs: String? = null,
    @Expose @SerializedName("languages") var languages: String? = null,
    @Expose @SerializedName("speaker_name") var speakerName: String? = null,
    @Expose @SerializedName("price") var price: String? = null,
    @Expose @SerializedName("start_date") var startDate: String? = null,
    @Expose @SerializedName("end_date") var endDate: String? = null,
    @Expose @SerializedName("no_of_attendees") var noOfAttendees: String? = null,
    @Expose @SerializedName("status") var status: String = SENT_FOR_APPROVAL,
    @Expose @SerializedName("is_verified") var isVerified: Int = 0,
    @Expose @SerializedName("is_rejected") var isRejected: Int = 0,


    @Expose(serialize = false, deserialize = false)
    @Transient var startTime: String? = null,
    @Expose(serialize = false, deserialize = false)
    @Transient var endTime: String? = null,


    @Transient var isApiRequestMade: Boolean = false
) {
/*
    companion object AddWebinarRequestBindingAdapter {
        @JvmStatic
        @InverseBindingAdapter(attribute = "android:text")
        fun putWebinarTitle(editText: EditText): String {
            var data = editText.text.toString()

            return data

        }
    }*/
}