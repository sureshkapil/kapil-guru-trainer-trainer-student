package com.kapilguru.trainer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import com.kapilguru.trainer.ui.courses.addcourse.models.LectureSyllabus
import com.kapilguru.trainer.ui.courses.addcourse.models.LectureSyllabusContent
import okhttp3.internal.UTC
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


//return true if email is not valid
fun String.emailValidation(): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z-_]+\\.+[a-z]+"
    return !(this.matches(emailRegex.toRegex()))
}

fun String.isPanValid(): Boolean {
    val pattern: Pattern = java.util.regex.Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
    return (this.matches(pattern.toRegex()))
}

fun String.isGstValid(): Boolean {
    val regex = "[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}"
    val pattern: Pattern = java.util.regex.Pattern.compile(regex)
    return (this.matches(pattern.toRegex()))
}

fun String.isStrongPassword(context: Context): Boolean {
    if (this.length < 7) {
        showAppToast(context, "Password must be atleast 8 characters")
        return false
    }
    val lowerCaseRegEx = ".*[a-z].*"
    val containsLowerCase = this.matches(lowerCaseRegEx.toRegex())
    if (!containsLowerCase) {
        showAppToast(context, "Password must contain at least 1 Letter in Small Case")
        return false
    }
    val upperCaseRegEx = ".*[A-Z].*"
    val containsUpperCase = this.matches(upperCaseRegEx.toRegex())
    if (!containsUpperCase) {
        showAppToast(context, "Password must contain at least 1 Letter in Upper Case")
        return false
    }
    val numberRegEx = ".*[0-9].*"
    val containsNumber = this.matches(numberRegEx.toRegex())
    if (!containsNumber) {
        showAppToast(context, "Password must contain at least 1 number")
        return false
    }
    val noSpecialCharacterRegEx = "[a-zA-Z0-9]+"
    val containsNoSpecialCharacter = this.matches(noSpecialCharacterRegEx.toRegex())
    if (containsNoSpecialCharacter) {
        showAppToast(context, "Password must contain at least 1 Special Character")
        return false
    }
    return true
}

// gives Date format
fun String.toDateFormat(): String? {
    val dateFormat: DateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T, Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("IST")
    val date: Date = dateFormat.parse(this)
//    val formatter: DateFormat = SimpleDateFormat("yyyy-MMM-dd", Locale.US)
    val formatter: DateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val dateEndStr: String? = formatter.format(date)
    return dateEndStr
}

fun String.toDateFormatWithOutT(): String? {
    val dateFormat: DateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T, Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("IST")
    val date: Date = dateFormat.parse(this)
//    val formatter: DateFormat = SimpleDateFormat("yyyy-MMM-dd", Locale.US)
    val formatter: DateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val dateEndStr: String? = formatter.format(date)
    return dateEndStr
}


fun String.toTimeFormatWithSecondsTo(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("dd MM yyyy HH:mm")
    formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
    return formatter.format(date)
}

fun String.toTimeFormat(): String? {
    val dateFormat: DateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
    dateFormat.timeZone = TimeZone.getTimeZone("IST")
    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("hh:mm a")
    return formatter.format(date)
}

fun String.toTimeFormatWithOutT(): String? {
    val dateFormat: DateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T)
    dateFormat.timeZone = TimeZone.getTimeZone("IST")
    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("hh:mm a")
    return formatter.format(date)
}

fun String.toDateFormatFromDateAndTimeWithSec(): String? {
    val givenFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    givenFormat.timeZone = TimeZone.getTimeZone("IST")
    val date: Date = givenFormat.parse(this)
    val requiredFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
    val dateEndStr: String? = requiredFormat.format(date)
    return dateEndStr
}

fun String.getEpochTime(): Long {
    val inputFormat: SimpleDateFormat = if (this.contains("T")) {
        SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
    } else {
        SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T)
    }
    inputFormat.timeZone = TimeZone.getTimeZone("IST")
    return inputFormat.parse(this).time
}

fun String.Companion.convertBase64ToString(base64String: String?): String {
    val byteArray = Base64.decode(base64String, Base64.DEFAULT)
    val decodedStr = String(byteArray)
    return decodedStr
}


fun String.toBase64(): String {
    val data = this.toByteArray(charset("UTF-8"))
    return Base64.encodeToString(data, Base64.DEFAULT)
}

fun String.fromBase64ToString(): String {
    val data: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return String(data, StandardCharsets.UTF_8)
}


fun String.Companion.convertStringToBase64(string: String): String {
    val byteArray = android.util.Base64.encode(string.toByteArray(), android.util.Base64.DEFAULT)
    val encodedStr = String(byteArray)
    return encodedStr
}


fun String.fromBase64(): String {
    val datasd = Base64.decode(this, Base64.DEFAULT)
    return String(datasd, charset("UTF-8"))
}


fun JSONArray.convertTOBase64(): String {
    val byteArray = android.util.Base64.encode(this.toString().toByteArray(), android.util.Base64.DEFAULT)
    val encodesStr = String(byteArray)
    return encodesStr
}

// using org.json;
fun LectureSyllabusContent.toJSONConverter(): JSONArray {
    val array: JSONArray = JSONArray()
    this.forEach {
        val jsonObject = JSONObject()
        jsonObject.put("syllabusHeading", it.syllabusHeading)
        jsonObject.put("syllabusSubHeading", it.syllabusSubHeading)
        array.put(jsonObject)
    }
    return array
}


// using org.json;
fun JSONArray.toLectureSyllabusContent(): LectureSyllabusContent {
    val lectureSyllabusContent = LectureSyllabusContent()
    for (i in 0 until this.length()) {
        val jSONObject = this.getJSONObject(i)
        val lectureSyllabus = LectureSyllabus(
            jSONObject.getInt("id"), jSONObject.getString("syllabusHeading"), jSONObject.getString("syllabusSubHeading")
        )
        lectureSyllabusContent.add(lectureSyllabus)
    }
    return lectureSyllabusContent
}


fun JSONObject.toBase64(): String {
    val formattedToString = this.toString()
    return formattedToString.toBase64()
}

fun String.toBitMap(): Bitmap? {
    val imageBytes = Base64.decode(this, Base64.DEFAULT)
    val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return image
}

fun String.toBitMapWithPadding(): Bitmap? {
    var imageBytes: ByteArray? = null
    var bitMapImage: Bitmap? = null
    val splitArray = this.split(",")

    imageBytes = if (splitArray.size > 1) {
        Base64.decode(splitArray[1], Base64.DEFAULT)
    } else {
        Base64.decode(splitArray[0], Base64.DEFAULT)
    }
    imageBytes?.let {
        bitMapImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    return bitMapImage
}

//returns true if mobile no is valid
fun String.isValidMobileNo(): Boolean {
    val reg = "^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}\$"
    val pattern: Pattern = Pattern.compile(reg)
    return pattern.matcher(this).find()
}

fun String?.isValidAadharNo(): Boolean {
    this?.let {
        return if (isStringEmpty()) {
            false
        } else {
            this.length == 12
        }
    } ?: kotlin.run {
        return false
    }
}

fun String?.isValidGST(): Boolean {
    this?.let {
        return if (isStringEmpty()) {
            false
        } else {
            val reg = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}\$"
            val pattern: Pattern = Pattern.compile(reg)
            return pattern.matcher(this).find()
        }
    } ?: kotlin.run {
        return false
    }
}

fun String?.isStringEmpty(): Boolean {
    this?.let {
        return TextUtils.isEmpty(this.trim())
    } ?: kotlin.run {
        return true
    }
}

fun String.toDecodeUTCToDate(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    dateFormat.timeZone = UTC

    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("yyyy-MMM-dd")
    val dateEndStr: String? = formatter.format(date)
    return dateEndStr
}

fun String.toDecodeUTCToTime(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    dateFormat.timeZone = UTC

    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("hh:mm: aa")
    val dateEndStr: String? = formatter.format(date)
    return dateEndStr
}

fun String.toDecodeUTCToISTTimeWithOutT(): String? {
    val dateFormat: DateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T, Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("IST")

    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("hh:mm: aa")
    val dateEndStr: String? = formatter.format(date)
    return dateEndStr
}

// used in Add Batch
fun String.toDecodeUTCToDateAndTime(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")

    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("yyyy/MM/dd, HH:mm: aa")
    val dateEndStr: String? = formatter.format(date)
    return dateEndStr
}

fun Calendar.convertDateAndTimeToApiData(): String {
    var dateString: String = ""
    val outputFmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    outputFmt.timeZone = TimeZone.getTimeZone("UTC")
//    calendar.timeZone = TimeZone.getTimeZone("UTC")
    val time = this.time
    dateString = outputFmt.format(time)
    return dateString
}

fun Calendar.convertDateAndTimeToApiDataWithoutT(): String {
    var dateString: String = ""
    val outputFmt = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T, Locale.US)
    outputFmt.timeZone = TimeZone.getTimeZone("UTC")
    val time = this.time
    dateString = outputFmt.format(time)
    return dateString
}

fun String?.apiDateFormatWithoutT(): String? {
    this?.let { it ->
        return if (doesApiDateContainsT(it)) {
            val dateFormat: DateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T, Locale.US)

            val date: Date = dateFormat.parse(it)
            val formatter: DateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T, Locale.US)
            //        val dateEndStr: String? = formatter.format(date)
            formatter.format(date)
        } else {
            it
        }
    } ?: run {
        return null
    }
}


fun doesApiDateContainsT(date: String) = date.contains("t", true)
