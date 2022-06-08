package com.kapilguru.trainer.ui.courses.add_batch.models

import com.google.gson.annotations.SerializedName

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
    @SerializedName("discounted_price")
    var discountedPrice: Int? = null,
    @SerializedName("no_of_days")
    var noOfDays: String? = null,
    @SerializedName("batch_type")
    var batchType: String? = null,
    @SerializedName("trainer_id")
    var trainerId: String? = null,
    @SerializedName("start_date")
    var startDate: String? = null,
    @SerializedName("batch_price")
    var batchPrice: Int? = null,
    @SerializedName("day_mon")
    var dayMon: Int? = null,
    @SerializedName("day_tue")
    var dayTue: Int? = null,
    @SerializedName("day_wed")
    var dayWed: Int? = null,
    @SerializedName("day_thu")
    var dayThu: Int? = null,
    @SerializedName("day_fri")
    var dayFri: Int? = null,
    @SerializedName("day_sat")
    var daySat: Int? = null,
    @SerializedName("day_sun")
    var daySun: Int? = null,
)