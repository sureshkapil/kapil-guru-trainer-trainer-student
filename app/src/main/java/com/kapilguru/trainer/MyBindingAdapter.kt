package com.kapilguru.trainer


import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.InverseBindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.textfield.TextInputEditText
import com.kapilguru.trainer.Companion.dateToString
import com.kapilguru.trainer.Companion.setFolderDrawable
import com.kapilguru.trainer.Companion.unWrapUTCTimeTOString
import com.kapilguru.trainer.student.exam.model.StudentQuestionsAndOptions
import com.kapilguru.trainer.studentExamBatchResult.QuestionPaperResponse
import kotlinx.android.synthetic.main.countryname_spinner_item.view.*
import kotlinx.android.synthetic.main.custom_merger_view.view.*
import kotlinx.android.synthetic.main.earnings_merger_view.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Companion {

    @JvmStatic
    @BindingAdapter("intToString")
    fun TextView.IntToString(id: Int?) {
        id?.let { it ->
            this.text = it.toString()
        } ?: run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("doubleToString")
    fun TextView.IntToString(id: Double?) {
        id?.let { it ->
            this.text = it.toString()
        } ?: run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("intToCount")
    fun TextView.IntToCount(id: Int?) {
        id?.let { it ->
            this.text = it.toString()
        } ?: run {
            this.text = "0"
        }
    }

    @JvmStatic
    @BindingAdapter("doubleToCount")
    fun TextView.doubleToCount(id: Double?) {
        id?.let { it ->
            val text = (it*100.0.roundToInt())/100.0
            this.text = text.toString()
        } ?: run {
            this.text = "0"
        }
    }

    @JvmStatic
    @BindingAdapter("daysToMonthsOrYears")
    fun TextView.daysToMonthsOrYears(days: Int?) {
        days.let { it ->
            val toShow = when (it) {
                365 -> "1 Year"
                180 -> "6 Months"
                90 -> "3 Months"
                60 -> "2 Months"
                30 -> "1 Month"
                else -> days.toString() + " days"
            }
            this.text = toShow
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["percentValue", "originalPrice"], requireAll = true)
    fun TextView.percentAmount(percentValue: Double?, originalPrice: Double?) {
        originalPrice?.let { originalPriceNotNull ->
            percentValue?.let { percentValueNotNull ->
                var amount = (originalPriceNotNull / 100) * percentValueNotNull
                amount = (amount*100.0).roundToInt()/100.0
                this.text = amount.toString()
            } ?: kotlin.run {
                this.text = originalPrice.toString()
            }
        } ?: kotlin.run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["cGstPercent", "sGstPercent", "amountBeforeGst"], requireAll = true)
    fun TextView.addGst(cGstPercent: Double?, sGstPercent: Double?, amountBeforeGst: Double?) {
        var cgst = 0.0
        var sgst = 0.0
        amountBeforeGst?.let { amountBeforeGstNotNull ->
            cGstPercent?.let { cGstNotNull ->
                cgst = (amountBeforeGstNotNull / 100) * cGstNotNull
            }
            sGstPercent?.let { sGstNotNull ->
                sgst = (amountBeforeGstNotNull / 100) * sGstNotNull
            }
            var amountAfterGST = amountBeforeGstNotNull + cgst + sgst
            amountAfterGST = (amountAfterGST *100.0).roundToInt()/100.0
            this.text = amountAfterGST.toString()
        } ?: kotlin.run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("appendDays")
    fun TextView.AppendDays(id: Int) {
        this.text = "$id days"
    }

    @JvmStatic
    @BindingAdapter("dateToString")
    fun TextView.dateToString(endd: String?) {
        endd?.let { current ->
            if (current.contains("T", true)) {
                current.toDateFormat()?.let {
                    this.text = it
                } ?: run {
                    this.text = ""
                }
            } else {
                current.toDateFormatWithOutT()?.let {
                    this.text = it
                } ?: run {
                    this.text = ""
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("dateTimeSecToDate")
    fun TextView.dateTimeSecToDate(endd: String) {
        endd.toDateFormatFromDateAndTimeWithSec()?.let {
            this.text = it
        }
    }

    @JvmStatic
    @BindingAdapter("timeToString")
    fun TextView.timeToString(endd: String?) {
        endd?.toTimeFormat()?.let {
            this.text = it
        } ?: kotlin.run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("renewalOrSubscribe")
    fun TextView.RenewalOrSubscribe(isOwned: Boolean) {
        if (isOwned) {
            this.text = "Renewal"
        } else {
            this.text = "Subscribe"
        }
    }


    @JvmStatic
    @BindingAdapter("lockedAmountTime")
    fun TextView.lockedAmountTime(time: String?) {
        time?.let { it ->
            val dateFormat: Date? = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(it)
            dateFormat?.let {
                val timFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(dateFormat)
                this.text = timFormat
            }
        }
    }

    @JvmStatic
    @BindingAdapter("lockedAmountDate")
    fun TextView.lockedAmountDate(date: String) {
        if (date.isEmpty()) {
            this.text = ""
        } else {
            val dateFormat: Date? = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)
            dateFormat?.let {
                val timFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(dateFormat)
                this.text = timFormat
            }
        }
    }

    @JvmStatic
    @BindingAdapter("timePatternHoursMinutes")
    fun TextView.formatTime(time: String?) {
        time?.let { it ->
            if (time.equals(null)) this.text = "NA"

            val dateFormat: Date? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH).parse(it)
            dateFormat?.let {
                val timFormat = SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.ENGLISH).format(dateFormat)
                this.text = timFormat
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["studentsCount", "maxStudents"], requireAll = true)
    fun TextView.BatchStatus(studentsCount: Int, maxNoOfStudents: Int) {
        this.text = "$studentsCount/$maxNoOfStudents"
    }

    @JvmStatic
    @BindingAdapter("coursePercentage")
    fun TextView.CoursePercentage(coursePercenatge: Int) {
        this.text = coursePercenatge.toString() + "%"
        fun TextView.CoursePercentage(coursePercenatge: Int) {
            this.text = "$coursePercenatge%"
        }
    }

    @JvmStatic
    @BindingAdapter("questionPaperStatus")
    fun TextView.QuestionPaperStatus(questionPaperStatus: String) {
        if (questionPaperStatus.equals("P")) {
            this.text = "Published"
        } else if (questionPaperStatus.equals("D")) {
            this.text = "Draft"
        }
    }

    @JvmStatic
    @BindingAdapter("questionPaperAction")
    fun TextView.QuestionPaperAction(questionPaperStatus: String) {
        if (questionPaperStatus.equals("P", true)) {
            this.text = "Copy"
        } else if (questionPaperStatus.equals("D", true)) {
            this.text = "View and Assign"
        }
    }

    @JvmStatic
    @BindingAdapter("base64ToHtml")
    fun TextView.base64ToString(base64String: String?) {
        base64String?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.text = Html.fromHtml(base64String.fromBase64(), Html.FROM_HTML_MODE_COMPACT)
            } else {
                this.text = Html.fromHtml(base64String.fromBase64())
            }
        } ?: kotlin.run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["optionNo", "correctOption"], requireAll = true)
    fun TextView.optionTextColor(optionNo: String, correctOption: String) {
        if (correctOption.equals(optionNo)) {
            this.setTextColor(ContextCompat.getColor(context, R.color.purple))
            this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_stroke_green)
        } else {
            this.setTextColor(ContextCompat.getColor(context, R.color.grey))
            this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_stroke_grey)
        }
    }

    @JvmStatic
    @BindingAdapter("courseStatus")
    fun TextView.courseStatus(courseStatus: Int) {
        if (courseStatus == 1) {
            this.text = "Approved"
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_approved_circle, 0, 0, 0)
        } else if (courseStatus == 0) {
            this.text = "Sent for approval"
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sent_for_approval_bg, 0, 0, 0)
        }
    }

    @JvmStatic
    @BindingAdapter("markOrMarks")
    fun TextView.Marks(marks: Int) {
        if (marks == 1) {
            this.text = "Mark"
        } else {
            this.text = "Marks"
        }
    }

    @JvmStatic
    @BindingAdapter("timeToString")
    fun CustomMergerText.timeToString(endd: String) {
        endd.toTimeFormat()?.let {
            this.text_value.text = it
        } ?: kotlin.run {
            this.text_value.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("indaysText")
    fun CustomMergerText.indaysText(endd: Int?) {
        endd?.let {
            this.text_value.text = this.context.resources.getString(R.string.duration_days, endd)
        } ?: kotlin.run {
            this.text_value.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("keyValueSquareViewTextToInt")
    fun keyValueSquareView.keyValueSquareViewTextToInt(endd: Int?) {
        endd?.let {
            this.text_value.text = endd.toString()
        } ?: kotlin.run {
            this.text_value.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("expiryDate")
    fun TextView.expiryDate(expiryDate: String?) {
        expiryDate?.let {
            this.text = "Expires on " + expiryDate.toDateFormatFromDateAndTimeWithSec()
        } ?: kotlin.run {
            this.text = ""


        }
    }

    @JvmStatic
    @BindingAdapter("adapterBase64ToString")
    fun TextView.adapterBase64ToString(base64String: String?) {
        base64String?.let { it ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.text = Html.fromHtml(it.fromBase64ToString(), Html.FROM_HTML_MODE_COMPACT)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("QuestionsBg")
    fun TextView.QuestionsBg(questionPaperResponse: QuestionPaperResponse?) {
        questionPaperResponse?.let { it ->
            when {
                it.selectedOpt == null -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_yellow_selection_bg)
                }
                it.correctOpt == it.selectedOpt -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_green_selection_bg)
                }
                it.correctOpt != it.selectedOpt -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_red_selection_bg)
                }
            }

        }
    }

    @JvmStatic
    @BindingAdapter("StudentQuestionsBg")
    fun AppCompatButton.StudentQuestionsBg(studentQuestionsAndOptions: StudentQuestionsAndOptions?) {
        studentQuestionsAndOptions?.let { it ->
            when {
                it.selectedOpt == null -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_yellow_selection_bg)
                }
                it.correctOpt == it.selectedOpt -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_green_selection_bg)
                }
                it.correctOpt != it.selectedOpt -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_red_selection_bg)
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["answersBackground", "answersOptionNumber"], requireAll = true)
    fun TextView.answersBackground(questionPaperResponse: QuestionPaperResponse?, answersOptionNumber: String) {
        questionPaperResponse?.let { it ->
            when {
                it.correctOpt == answersOptionNumber -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_green_solid_bg)
                    this.setTextColor(ContextCompat.getColor(this.context, R.color.purple))
                }
                it.selectedOpt == answersOptionNumber -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_red_solid_bg)
                    this.setTextColor(ContextCompat.getColor(this.context, R.color.purple))
                }
                else -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_stroke_grey)
                    this.setTextColor(ContextCompat.getColor(this.context, R.color.black))
                }
            }
        }

    }

    @JvmStatic
    @BindingAdapter(value = ["answersBackground", "answersOptionNumber"], requireAll = true)
    fun TextView.answersBackgroundStudent(studentQuestionsAndOptions: StudentQuestionsAndOptions?, answersOptionNumber: String) {
        studentQuestionsAndOptions?.let { it ->
            when {
                it.correctOpt == answersOptionNumber -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_green_solid_bg)
                    this.setTextColor(ContextCompat.getColor(this.context, R.color.purple))
                }
                it.selectedOpt == answersOptionNumber -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_red_solid_bg)
                    this.setTextColor(ContextCompat.getColor(this.context, R.color.purple))
                }
                else -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_stroke_grey)
                    this.setTextColor(ContextCompat.getColor(this.context, R.color.black))
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("loadGlideImage")
    fun ImageView.loadGlideImage(imageUrl: String?) {
        imageUrl?.let { it ->
            when {
                TextUtils.equals(it, "null") -> this.setImageResource(R.drawable.default_image)
                TextUtils.equals(it, "") -> this.setImageResource(R.drawable.default_image)
                imageUrl.contains(".") -> Glide.with(context).load(IMAGE_BASE_URL.plus(it)).placeholder(ResourcesCompat.getDrawable(resources, R.drawable.default_image, null)).into(this)
                else -> Glide.with(context).load(IMAGE_BASE_URL.plus(it).plus(".png")).into(this)
            }
        }?:run {
            this.setImageResource(R.drawable.default_image)
        }
    }

    @JvmStatic
    @BindingAdapter("loadGlideNewImage")
    fun ImageView.loadGlideNewImage(imageUrl: String?) {
        imageUrl?.let { it ->
            when {
                TextUtils.equals(it, "null") -> this.setImageResource(R.drawable.default_image)
                imageUrl.contains(".") -> Glide.with(context).load(IMAGE_BASE_URL.plus(it)).placeholder(
                    ResourcesCompat.getDrawable(resources, R.drawable.default_image, null)).diskCacheStrategy(
                    DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
                else -> Glide.with(context).load(IMAGE_BASE_URL.plus(it).plus(".png")).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
            }
        } ?: run {
            this.setImageResource(R.drawable.default_image)
        }
    }

    @JvmStatic
    @BindingAdapter("fetchImage")
    fun ImageView.fetchImage(imageUrl: String?) {
        imageUrl?.let { it ->
            if (TextUtils.equals(it, null)) this.setImageResource(R.drawable.profile)
            else Glide.with(context).load(IMAGE_BASE_URL.plus(it)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["isPositionOccupied", "positionOfTextView", "selectedPositionToSubscribe"], requireAll = true)
    fun TextView.setPositionSubscriptionBackGround(isPositionOccupied: Boolean, positionOfTextView: String, selectedPositionToSubscribe: String) {
        if (isPositionOccupied) {
            this.background = AppCompatResources.getDrawable(this.context, R.drawable.position_occupied)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.setTextColor(resources.getColor(R.color.grey_3, null))
            } else {
                this.setTextColor(resources.getColor(R.color.black))
            }
        } else {
            if (TextUtils.equals(positionOfTextView, selectedPositionToSubscribe)) {
                this.background = AppCompatResources.getDrawable(this.context, R.drawable.position_unoccupied_selected)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.setTextColor(resources.getColor(R.color.white, null))
                } else {
                    this.setTextColor(resources.getColor(R.color.white))
                }
            } else {
                this.background = AppCompatResources.getDrawable(this.context, R.drawable.position_unoccupied_unselected)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.setTextColor(resources.getColor(R.color.blue_3, null))
                } else {
                    this.setTextColor(resources.getColor(R.color.blue_3))
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("UTCFormatToDate")
    fun TextView.UTCFormatToDate(date: String?) {
        date?.let {
            this.text = it.toDecodeUTCToDate()
        }
    }

    @JvmStatic
    @BindingAdapter("UTCFormatToTime")
    fun TextView.UTCFormatToTime(date: String?) {
        date?.let {
            if(date.contains('T',true))
                this.text = it.toDecodeUTCToTime()
            else
                this.text = it.toDecodeUTCToISTTimeWithOutT()
        }
    }

    @JvmStatic
    @BindingAdapter("capitalizeFirstLetter")
    fun TextView.capitalizeFirstLetter(date: String?) {
        date?.let {
            val sb = StringBuilder(it)
            sb.setCharAt(0, Character.toUpperCase(sb[0]))
            text = sb.toString()
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["courseAmount", "referralAmount", "webinarAmount", "isRequestMoneyAvailable"], requireAll = true)
    fun EarningsMergerView.setTotal(courseAmount: Double?, referralAmount: Double?, webinarAmount: Double?, isRequestMoneyAvailable: Boolean) {
        var sum = 0.0
        courseAmount?.let {
            this.course_amount.text = getAmountInRupees(it)
            sum += it
        }
        referralAmount?.let {
            this.referral_amount.text = getAmountInRupees(it)
            sum += it
        }
        webinarAmount?.let {
            this.webinar_amount.text = getAmountInRupees(it)
            sum += it
        }
        this.amount.text = getAmountInRupees(sum)
        this.request_money.visibility = if (isRequestMoneyAvailable) View.VISIBLE else View.GONE
    }

    private fun EarningsMergerView.getAmountInRupees(it: Double) = resources.getString(R.string.rupee_symbol, it)


    @JvmStatic
    @BindingAdapter("unWrapUTCToDate")
    fun TextView.unWrapUTCToDate(date: String?) {
        date?.toDateFormatWithOutT()?.let {
            this.text = it
        } ?: run {
            this.text = ""
        }

    }

    @JvmStatic
    @BindingAdapter("unWrapUTCToTime")
    fun TextView.unWrapUTCToTime(date: String?) {
        date?.toTimeFormatWithOutT()?.let {
            this.text = it
        } ?: run {
            this.text = ""
        }
    }


    @JvmStatic
    @BindingAdapter("amountInRupees")
    fun TextView.amountInRupees(amount: Double?) {
        amount?.let {
            this.text = resources.getString(R.string.rupee_symbol, it)
        } ?: run {
            this.text = resources.getString(R.string.rupee_symbol, 0.0)
        }
    }

    @JvmStatic
    @BindingAdapter("myReferralStatus")
    fun TextView.myReferralStatus(expiryDate: String?) {
        expiryDate?.let { expiryDateNotNull ->
            val dateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
            val date = dateFormat.parse(expiryDateNotNull)
            date?.let {
                if (it.time > System.currentTimeMillis()) {
                    this.text = resources.getString(R.string.active)
                    this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_approved_circle, 0, 0, 0)
                } else {
                    this.text = resources.getString(R.string.expired)
                    this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_rejected_circle, 0, 0, 0)
                }
            }
        } ?: run {
            this.text = resources.getString(R.string.expired)
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_rejected_circle, 0, 0, 0)
        }
    }

    @JvmStatic
    @BindingAdapter("keyValueCourseDuaration")
    fun KeyValueText.setStringToIntCourseDuration(days: Int?) {
        days?.let {
            this.text_value.text = getPluralDays(this.context,it)
        } ?: run {
            this.text_value.text = getSingularDay(this.context,0)
        }
    }

    private fun getPluralDays(context: Context, days: Int?) = context.resources.getString(R.string.duration_days, days)

    private fun getSingularDay(context: Context,days: Int) = context.resources.getString(R.string.duration_day, days)

    @JvmStatic
    @BindingAdapter(value = ["experienceYears"], requireAll = false)
    fun KeyValueText.setExperience(years: Int?) {
        years?.let {
            if (it > 1) {
                this.text_value.text = this.resources.getString(R.string.append_years, years)
            } else {
                this.text_value.text = this.resources.getString(R.string.append_year, years)
            }
        } ?: run {
            this.text_value.text = this.resources.getString(R.string.append_year, 0)
        }
    }


    @JvmStatic
    @BindingAdapter("keyValueToInt")
    fun KeyValueText.setStringToInt(toBeStringData: Int?) {
        toBeStringData?.let {
            this.text_value.text = it.toString()
        } ?: run {
            this.text_value.text = "0"
        }
    }


    @JvmStatic
    @BindingAdapter("unWrapUTCTimeTOString")
    fun KeyValueText.unWrapUTCTimeTOString(date: String?) {
        date?.toTimeFormat()?.let {
            this.text_value.text = it
        } ?: run {
            this.text_value.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("setKeyValueTextString")
    fun KeyValueText.setKeyValueTextString(toBeStringData: String?) {
        toBeStringData?.let {
            this.text_value.text = it.toString()
        } ?: run {
            this.text_value.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("setKeyTextString")
    fun KeyValueText.setKeyTextString(toBeStringData: String?) {
        toBeStringData?.let {
            this.text_key.text = it
        } ?: run {
            this.text_key.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("setKeyStudentNameOrPublic")
    fun KeyValueText.setKeyStudentNameOrPublic(toBeStringData: String?) {
        toBeStringData?.let {
            this.text_value.text = it.toString()
        } ?: run {
            this.text_value.text = resources.getString(R.string._public)
        }
    }

    @JvmStatic
    @BindingAdapter("dateToString")
    fun KeyValueText.dateToString(endd: String?) {
        endd?.let { current ->
            if (current.contains("T", true)) {
                current.toDateFormat()?.let {
                    this.text_value.text = it
                } ?: run {
                    this.text_value.text = ""
                }
            } else {
                current.toDateFormatWithOutT()?.let {
                    this.text_value.text = it
                } ?: run {
                    this.text_value.text = ""
                }
            }
        }
    }


    @JvmStatic
    @BindingAdapter("editDateToString")
    fun TextInputEditText.dateToString(endd: String?) {
        endd?.let { current ->
            if (current.contains("T", true)) {
                current.toDateFormat()?.let {
                    this.setText(it)
                } ?: run {
                    this.setText("")
                }
            } else {
                current.toDateFormatWithOutT()?.let {
                    this.setText(it)
                } ?: run {
                    this.setText("")
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("keyValueToIntPercentage")
    fun KeyValueText.keyValueToIntPercentage(toBeStringData: Int?) {
        toBeStringData?.let {
            this.text_value.text = resources.getString(R.string.percentage_value,it)
        } ?: run {
            this.text_value.text =  resources.getString(R.string.percentage_value, 0)
        }
    }

    @JvmStatic
    @BindingAdapter("setBatchType")
    fun KeyValueText.setBatchType(batchType: String?) {
        batchType?.let {
            if(batchType.equals("both", true))
                text_value.text = resources.getString(R.string.weekday_weekend)
            else
                text_value.text = batchType
        } ?: run {
            text_value.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("setBatchType")
    fun AppCompatTextView.setBatchType(batchType: String?) {
        batchType?.let { it->
            text = if(it.equals("both", true))
                resources.getString(R.string.weekday_weekend)
            else
                it
        } ?: run {
            text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("amountInRupeesStrike")
    fun TextView.amountInRupeesStrike(amount: Double?) {
        amount?.let {
            this.text = resources.getString(R.string.rupee_symbol_strike, it.toString())
            this.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } ?: run {
            this.text = resources.getString(R.string.rupee_symbol_strike, "0")
            this.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }


    @JvmStatic
    @BindingAdapter("amountInRupees")
    fun KeyValueText.amountInRupees(amount: Double?) {
        amount?.let {
            this.text_value.text = rupeeSymbolWithInt(this.context,it.toInt())
        } ?: run {
            this.text_value.text = rupeeToZero(this.context)
        }
    }

    private fun rupeeToZero(context: Context) = context.resources.getString(R.string.rupee_symbol_without_float, "0")

    private fun rupeeSymbolWithInt(context: Context, it: Int) = context.resources.getString(R.string.rupee_symbol_without_float, it.toString())

    @JvmStatic
    @BindingAdapter("sessionsInDays")
    fun TextView.sessions(days: Int?) {
        days?.let {
            Log.d("TAG", "sessions: $days")
            if (it > 1) {
                this.text = getPluralDays(this.context, it)
            } else {
                this.text = getSingularDay(this.context, it)
            }
        } ?: run {
            this.text = getSingularDay(this.context,0)
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["offerPrice","originalAmount","isInternetAdded","taxPercentage"], requireAll = true)
    fun TextInputEditText.addTaxOnDiscountAmount(offerPrice: Double?,originalAmount: Double?,isInternetAdded: Boolean, taxPercentage:Double?) {
      /*  if(isInternetAdded) {
            offerPrice?.let {

            }
            afterDiscountAmount?.let {amount ->
                if(amount>0 && internetPercentage!!>0) {
                    val finalAmount = amount + (amount/internetPercentage)
                    this.setText(finalAmount.toString())
                }
            }
        } else {
            afterDiscountAmount?.let {amount ->
                this.setText(afterDiscountAmount.toString())
            }
        }
        afterDiscountAmount?.let { it ->
            val text = afterDiscountAmount +(it*100.0.roundToInt())/100.0
            this.text = text.toString()
        } ?: run {
            this.setText("0.0")
        }*/
    }

    @JvmStatic
    @BindingAdapter("texteditDoubleToCount")
    fun TextInputEditText.textInputDoubleToCount(id: Double?) {
        id?.let { it ->
            val txt = (it*100.0.roundToInt())/100.0
            this.setText( txt.toString())
        } ?: run {
            this.setText("0")
        }
    }

    @JvmStatic
    @BindingAdapter("underlinetext")
    fun TextView.underlinetext(boolean: Boolean) {
        this.paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    @JvmStatic
    @BindingAdapter(value = ["isExamAttempted","isExamCompleted"],requireAll = true)
    fun TextView.ExamAction(isExamAttempted : Int, isExamCompleted : Int) {
        if(isExamCompleted ==1){
            this.text = resources.getString(R.string.view_result)
        }else{
//            if(isExamAttempted == 0){
            this.text = resources.getString(R.string.start_exam)
//            }else{
//                this.text = resources.getString(R.string.continue_text)
//            }
            }
    }

    @JvmStatic
    @BindingAdapter("textIntToString")
    fun TextInputEditText.textIntToString(id: Int?) {
        id?.let { it ->
            this.setText(it.toString())
        } ?: run {
            this.setText("0")
        }
    }

    @JvmStatic
    @BindingAdapter("customchecked")
    fun SwitchCompat.abc(id: Int?) {
        id?.let { it ->
            this.isChecked = it != 0
        } ?: run {
            this.isChecked = false
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "customchecked", event = "android:checkedAttrChanged")
    fun abcInverse(switchCompat: SwitchCompat):Int {
        return if(switchCompat.isChecked) 1 else 0
    }


    @JvmStatic
    @BindingAdapter("appendMins")
    fun TextView.AppendMins(id: Int) {
        this.text = "$id Mins"
    }


    @JvmStatic
    @BindingAdapter("isonline")
    fun AppCompatTextView.isonline(id: Int?) {
        id?.let {it->
            when(it) {
                0 -> {
                    this.text = "Offline"
                    this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.disable_background, 0, 0, 0);

                }
                1-> {
                    this.text = "OnLine"
                    this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_approved_circle_small, 0, 0, 0);

                }
                2-> {
                    this.text = "Online/Offline"
                    this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_approved_circle_small, 0, 0, 0);
                }
            }
        }


    }

    @JvmStatic
    @BindingAdapter(value = ["folderDrawable", "fileType"], requireAll = true)
    fun AppCompatImageView.setFolderDrawable(folderDrawable: Int?, fileType: String?) {
        folderDrawable?.let { it ->
            if (it == 1) {
                this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_folder_24))
            } else {
                fileType?.let {
                    if (it.contains("pdf", true)) {
                        this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_picture_as_pdf_24))
                    } else {
                        this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_ondemand_video_24))
                    }
                } ?: run {

                }
            }
        } ?: run {
//            this.text = getSingularDay(this.context,0)
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["videoCount", "docCount", "tpCount"], requireAll = true)
    fun AppCompatTextView.setCountOfvideo(videoCount: Int?, docCount: Int?, tpCount: Int?) {
        var contentCountTxt: String = ""
        videoCount?.let {video ->
            if(video>0) {
                contentCountTxt = "$videoCount Videos,"
            }
        }
        docCount?.let {docCOunt ->
            if(docCOunt>0) {
                contentCountTxt = "$videoCount Videos,"
            }
        }
        tpCount?.let {tpCount ->
            if(tpCount>0) {
                contentCountTxt = "$videoCount Videos,"
            }
        }
        if(contentCountTxt.trim().isNotEmpty()) {
            this.text = contentCountTxt.trim()
        } else {
            this.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("enquiryMail")
    fun AppCompatTextView.enquiryMail(mail : String?){
        if(mail.isStringEmpty()){
            this.text = "NA"
        }else{
            this.text = mail
        }
    }
}