package com.kapilguru.trainer.ui.courses.batchesList.models

import android.util.Log
import androidx.annotation.Keep
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Keep
data class BatchListApiResponseData(
    val batch_id: Int,
    val batch_type: String,
    val course_completion_count: Int,
    val course_id: Int,
    val course_title: String,
    val current_no_of_students: Int,
    val duration: Int,
    val end_date: String,
    val fee: Int,
    val fri: Int,
    val is_active: Int,
    val is_batch_filled: Int,
    val max_no_of_students: Int,
    val mon: Int,
    val offer_price: Int,
    val percent_completion: Int,
    val refunded_count: Int,
    val sat: Int,
    val start_date: String,
    val students_count: Int,
    val sun: Int,
    val thu: Int,
    val trainer_id: Int,
    val trainer_name: String,
    val tue: Int,
    val wed: Int
) {
    fun getConvertedBatchStartDate(): String {
        val dateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        //You will get date object relative to server/client timezone wherever it is parsed
        val date: Date = dateFormat.parse(start_date)
        //If you need time just put specific format for time like 'HH:mm:ss'
        val formatter: DateFormat =
            SimpleDateFormat("yyyy-MM-dd")
        //If you need time just put specific format for time like 'HH:mm:ss'
        val dateStr: String? = formatter.format(date)
        Log.v("Check_info", dateStr!!)
        return dateStr
    }
}