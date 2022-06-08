package com.kapilguru.trainer.ui.courses.addcourse.models

import android.util.Log
import android.widget.EditText
import androidx.annotation.Keep
import androidx.databinding.InverseBindingAdapter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class AddCourseRequest(
        @SerializedName("about_trainer") var aboutTrainer: String?=null,
        @SerializedName("batchtype") var batchType: String?="Weekday", //Weekend, Weekday, both
        @SerializedName("category_id") var categoryID: String ?= null,
        @SerializedName("course_image") var courseImage: String?=null,
        @SerializedName("course_sub_title") var courseSubTitle: String?=null,
        @SerializedName("course_title") var courseTitle: String?=null,
        @SerializedName("demo_video") var demoVideo: String?=null,
        @SerializedName("course_video") var courseVideo: String?=null,
        @SerializedName("description") var description: String?=null,
        @SerializedName("fee") var fee: String?=null,
        @SerializedName("actual_fee") var actualFee: String?=null,
        @SerializedName("duration_days") var durationDays: String?=null,
//        @SerializedName("fee") var fee: String?="ashsg",
        @SerializedName("language") var language: String?=null,
        @SerializedName("syllabus_attachment") var syllabusAttachment: Int?=null,
        @SerializedName("syllabus_type") var syllabusType: String?=null,
        @SerializedName("syllabus_text_content") var syllabusTextContent: String?=null,
        @SerializedName("total_no_of_students_trained") var totalNoOfStudentsTrained: String?=null,
        @SerializedName("trainer_id") var trainerID: String?=null,
        @SerializedName("trainer_name") var trainerName: String?=null,
        @SerializedName("trainers_year_of_exp") var trainersYearOfExp: String?=null,
        @SerializedName("code") var code: String?=null,
        @SerializedName("is_verified") var isVerified: Int=0,
        @SerializedName("is_rejected") var isRejected: Int=0,
        @SerializedName("is_submitted") var is_verified: Int=1,
        @Transient var isApiRequestMade: Boolean = false,
) {


  /*  companion object AddCourseRequestBindingAdapter {

        @JvmStatic
        @InverseBindingAdapter(attribute = "android:text")
        fun putCourseTitle(editText: EditText) : String {
        Log.v("checkifo","inverse")
            val data = editText.text.toString()

            return data;
        }

    }*/

}