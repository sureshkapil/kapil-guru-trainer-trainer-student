package com.kapilguru.trainer.ui.courses.add_batch.models

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.APP_WEEKDAY

data class AddBatchRequest(
    @SerializedName("end_date")
    var endDate: String? = null,
    @SerializedName("course_id")
    var courseId: Int? = null,
    @SerializedName("class_duration")
    var classDuration: String? = null,
    @SerializedName("dates_json")
    var datesJson: String? = null,
    @SerializedName("time_zone")
    var timeZone: String? = "IST",
    @SerializedName("max_no_of_students")
    var maxNoOfStudents: String? = null,
    @SerializedName("no_of_days")
    var noOfDays: String? = null,
    @SerializedName("batch_type")
    var batchType: String? = APP_WEEKDAY,
    @SerializedName("trainer_id")
    var trainerId: String? = null,
    @SerializedName("start_date")
    var startDate: String? = null,
    @SerializedName("day_mon")
    var dayMon: Int? = 1,
    @SerializedName("day_tue")
    var dayTue: Int? = 1,
    @SerializedName("day_wed")
    var dayWed: Int? = 1,
    @SerializedName("day_thu")
    var dayThu: Int? = 1,
    @SerializedName("day_fri")
    var dayFri: Int? = 1,
    @SerializedName("day_sat")
    var daySat: Int? = null,
    @SerializedName("day_sun")
    var daySun: Int? = null,
    @SerializedName("discount_amount")
    var discountAmount: Double? = null,
    @SerializedName("discounted_price")
    var discountedPrice: Double? = null,
    @SerializedName("batch_price")
    var batchPrice: Double? = null,
    @SerializedName("is_tax")
    var isTax: Int? = 0,
    @SerializedName("internet_charges")
    var internetCharges: Double?=null,
    @SerializedName("is_online")
    var isOnline: Int?=1,
    @SerializedName("is_show_in_web")
    var isKgMeeting: Int?=0,
    )