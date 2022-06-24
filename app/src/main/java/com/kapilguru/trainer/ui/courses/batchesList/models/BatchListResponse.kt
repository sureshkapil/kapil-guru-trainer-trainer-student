package com.kapilguru.trainer.ui.courses.batchesList.models

import android.util.Log
import android.widget.TextView
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.R
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Keep
data class BatchListResponse(
    @SerializedName("batch_id") val batchId: Int,
    @SerializedName("batch_code") val batchCode: String,
    @SerializedName("batch_type") val batchType: String,
    @SerializedName("course_completion_count") val courseCompletionCount: Int,
    @SerializedName("course_id") val courseId: Int,
    @SerializedName("course_title") val courseTitle: String,
    @SerializedName("course_code") val courseCode: String,
    @SerializedName("current_no_of_students") val currentNoOfStudents: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("fee") val fee: Double?,
    @SerializedName("fri") val fri: Int,
    @SerializedName("is_active") val isActive: Int,
    @SerializedName("is_batch_filled") val isBatchFilled: Int,
    @SerializedName("max_no_of_students") val maxNoOfStudents: Int,
    @SerializedName("mon") val mon: Int,
    @SerializedName("offer_price") val offerPrice: Double?,
    @SerializedName("percent_completion") val percentCompletion: Int?,
    @SerializedName("refunded_count") val refundedCount: Int,
    @SerializedName("sat") val sat: Int,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("students_count") var studentsCount: Int,
    @SerializedName("sun") val sun: Int,
    @SerializedName("thu") val thu: Int,
    @SerializedName("trainer_id") val trainerId: Int,
    @SerializedName("trainer_name") val trainerName: String?,
    @SerializedName("tue") val tue: Int,
    @SerializedName("wed") val wed: Int,
    var shouldShowFooter : Boolean = false
) : Serializable {
    fun getConvertedBatchStartDate(): String {
        val dateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        //You will get date object relative to server/client timezone wherever it is parsed
        val date: Date = dateFormat.parse(startDate)
        //If you need time just put specific format for time like 'HH:mm:ss'
        val formatter: DateFormat =
            SimpleDateFormat("yyyy-MM-dd")
        //If you need time just put specific format for time like 'HH:mm:ss'
        val dateStr: String? = formatter.format(date)
        Log.v("Check_info", dateStr!!)
        return dateStr
    }


    companion object  bindingAdapterData {
        @JvmStatic
        @BindingAdapter("batch_day")
        fun setBatchDay(textView: TextView, batchDay: Int) {
            if (batchDay == 1) {
                textView.background = textView.context.getDrawable(R.drawable.selected_weekdays_bg)
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.white));
            } else {
                textView.background =
                    textView.context.getDrawable(R.drawable.unselected_weekdays_bg)
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.black));
            }
        }
    }
}