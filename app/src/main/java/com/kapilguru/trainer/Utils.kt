package com.kapilguru.trainer

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.kapilguru.trainer.preferences.StorePreferences
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

// Production URl
//const val BASE_URL = "https://kapilguru.com/kg-v1/"
//const val SHARE_URL = "https://kapilguru.com/"
//const val IMAGE_BASE_URL = BASE_URL+"images/"
//const val VIDEO_BASE_URL = BASE_URL+"videos/"

// Test Url
//const val BASE_URL = "https://beta.kapilguru.com/kg-v1/"
//const val SHARE_URL = "https://beta.kapilguru.com/"
const val IMAGE_BASE_URL = BuildConfig.SHARE_URL + "images/"
const val VIDEO_BASE_URL = BuildConfig.SHARE_URL + "videos/"

// Dev Urls
/*const val BASE_URL = "http://44.197.234.100:9000/"
const val SHARE_URL = "http://44.197.234.100:4200/"
const val IMAGE_BASE_URL = "http://44.197.234.100/kg/images/"
const val VIDEO_BASE_URL = "http://44.197.234.100/kg/videos/"*/

const val COURSE_DETAILS = "course-details/"
const val WEBINAR_DETAILS = "webinar-details/"
const val DEMO_LECTURES_DETAILS = "demo-lectures-details/"
const val COURSE_FILE = "courseFiles/files/"
const val SHARED_PREFERENCE_FILE = "sharedPreference"
const val JWT_TOKEN = "jwtToken"
const val JWT_REFRESH_TOKEN = "jwtRefreshToken"
const val USER_ID = "userId"
const val USER_EMAIL = "user_email"
const val USER_ROLE_ID = "user_role_id"
const val USER_IMAGE = "user_image"
const val TITLE = "title"
const val COUNTRY_CODE = "country_code"
const val IS_PROFILE_UPDATED = "isProfileUpdated"
const val IS_ENROLLED = "isEnrolled"
const val CURRENCY = "currency"
const val CONTACT_NO = "contactNumber"
const val APP_WEEKDAY = "Weekday"
const val APP_WEEKEND = "Weekend"
const val UPDATE_BANK_ID = "updateBankId"
const val IS_BANK_UPDATED = "isBankUpdated"
const val IS_IMAGE_UPDATED = "isImageUpdated"
const val COURSE_INFO_PARAM = "courseInfo"
const val IS_FROM_EDIT_PARAM = "isFromEdit"
const val COURSE_ID_PARAM = "courseId"
const val EDIT_BATCH_ID_PARAM = "editBatchId"
const val WEBINAR_EDIT_ID_PARAM = "webinarEditId"
const val WEBINAR_EDIT_KEY_PARAM = "webinarEditKey"
const val COURSE_NAME_PARAM = "courseName"
const val IS_VERIFIED = 1
const val IS_SUBSCRIBED = "isSubscribed"
const val TENANT_ID = "tenantId"
const val IS_MARKETING = "isMarketing"
const val IS_KYC_UPDATED = "isKYCUpdated"
const val IS_ORGANIZATION = "isOrganization"
const val LAST_ANNOUNCEMENT_ID = "lastAnnouncementId"
const val IS_OTHER_COUNTRY_CODE = "isOtherCountryCode"
const val USER_NAME = "userName"
const val USER_CODE = "user_code"
const val Add_COURSE_IMAGE_MAX = 3072
const val ADD_COURSE_IMAGE_MIN = Add_COURSE_IMAGE_MAX
const val ADD_COURSE_WINDOW = 256
const val Add_WEBINAR_IMAGE_MAX = 3072
const val ADD_WEBINAR_IMAGE_MIN = Add_COURSE_IMAGE_MAX
const val ADD_WEBINAR_WINDOW_X = 365
const val ADD_WEBINAR_WINDOW_Y = 182
const val ADD_GUEST_LECTURE_WINDOW_X = 358
const val ADD_GUEST_LECTURE_WINDOW_Y = 199
const val VIEW_SELECTED_LOCKED_AMOUNT = "viewSelectedLockedAmount"
const val COMPLAINT_STUDENT_ID = "complaintStudentId"
const val REQUEST_ID_PARAM = "requestIdParam"
const val Batch_Reports_Request_PARAM = "batchReportsRequestModel"
const val Batch_STUDENT_Reports_Request_PARAM = "batchStudentReportsRequestModel"
const val DIALOG_SUBTITLE_PARAM = "dialogSubTitle"
const val PARAM_IS_FROM = "is_from"
const val PARAM_ANNOUNCEMNT_ID = "announcement_id"
const val PARAM_IS_FROM_DASHBOARD = "dashboard"
const val PARAM_IS_FROM_COURSE = "course"
const val PARAM_IS_FROM_BATCH = "batch"
const val PARAM_COURSE_ID = "course_id"
const val PARAM_IS_FROM_HOME_ACTIVITY = "bell"
const val COURSE_TITLE_PARAM = "course_title"
const val PARAM_BATCH_CODE = "batch_code"
const val STUDENT_NAME_PARAM = "student_name"
const val STUDENT_CODE_PARAM = "student_code"
const val PARAM_DEMO_WEBINAR_ID = "Param_webinar_id"
const val PARAM_DEMO_WEBINAR_TITILE = "Param_webinar_title"
const val PARAM_IS_FROM_DEMO = "demo"
const val PARAM_IS_FROM_WEBINAR = "webinar"
const val SENT_FOR_APPROVAL = "Sent for Approval"
const val APPROVED = "Approved"
const val PNG = ".png"
const val MP4 = ".mp4"
const val WEBINAR = "webinar"
const val LECTURE = "lecture"
const val COURSE = "course"
const val PARAM_BATCH_ID = "batch_id"
const val API_FORMAT_DATE_AND_TIME_WITH_T = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val API_FORMAT_DATE_AND_TIME_WITHOUT_T = "yyyy-MM-dd HH:mm:ss"
const val UI_FORMAT_TIME = "hh:mm a"
const val PARAM_IS_AVAILABLE = "is_available" // used this to read available or expected from parsing json
const val PARAM_IS_FROM_EARNINGS = "earnings"
const val PARAM_IS_FROM_EARNINGS_HISTORY = "history"
const val PARAM_IS_FROM_COURSE_PAYMENT_LIST = "course_payment"
const val PARAM_AMOUNT_LIST_INFO = "amount_list_info"
const val SUBSCRIPTION_PACKAGE = "package"
const val SUBSCRIPTION_POSITION = "position"
const val SUBSCRIPTION_BADGE = " best_trainer"
const val SUBSCRIPTION_SUB_TYPE_BADGE = "badge"
const val SUBSCRIPTION_CATEGORY_INDIVIDUAL = "individual"
const val SUBSCRIPTION_CATEGORY_ORGANISATION = "organisation"
const val TRANSACTION_SUCCESS = "TXN_SUCCESS"
const val PARAM_TODAYS_SCHEDULE_NOTIFICATION = "param_todays_schedule_notification"
const val SESSION_OUT = 401
const val MAX_VIDEO_FILE_SIZE: Int = 150 * 1024 * 1024
const val PARAM_PROFILE: String = "param_profile"
const val INDIAN_COUNTRY_CODE = "91"
const val PDF_URL = "courseFiles/files/"
const val PDF_PARAM = "pdf_param"
const val PARAM_COURSE_SYLLABUS = "paramCourseSyllabus"
const val PARAM_COURSE_DESCRIPTION = "paramCourseDescription"
const val NETWORK_CONNECTIVITY_EROR = 800
const val FACEBOOK_URL = "https://www.facebook.com/KapilGuruLearningApp/?ref=pages_you_manage"
const val TWITTER_URL = "https://twitter.com/kapil_guru"
const val INSTAGRAM_URL = "https://www.instagram.com/accounts/login/?next=/kapilguru.liveonlinetraining/"
const val LINKED_IN_URL = "https://in.linkedin.com/company/kapil-guru"
const val YOUTUBE_URL = "https://www.youtube.com/channel/UCk2-2a8XTUMrya5YQrE4SAw"
const val DIALOG_FRAGMENT_TAG_PROFILE: String = "dialog_fragment_tag_profile"
const val PARAM_QUESTIONS_REQUEST = "questionsRequest"
const val PARAM_REPORTS_REQUEST = "repostRequest"


fun showAppToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

fun showAppToastCenter(context: Context, text: String) {
    val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun generateUuid() = UUID.randomUUID()


/**
 * Calculate the Time difference between two dates and Return data in milliseconds
 **/
fun datesDifferenceInMilli(batchStartDate: String): Long {
    val cal = Calendar.getInstance()
    cal.timeZone.id = "Asia/Kolkata"
    val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm")
    val cal2 = Calendar.getInstance()
    cal2.time = dateFormat.parse(batchStartDate.toTimeFormatWithSecondsTo())
    return (cal2.timeInMillis - cal.timeInMillis)
}


/**
 * Here minutes % 60 return Minutes
 * Here minutes / 60 return Hours
 *
 **/
fun liveClassMinutesLimit(milli: Long): Boolean {
    val seconds = (milli / 1000).toInt()
    val minutes = seconds / 60
//    Log.d("TAG_ABC", "getMinutesFromMillis: ${minutes % 60}")
//    Log.d("TAG_ABC", "getMinutesFromMillis: ${minutes / 60}")
    return minutes / 60 <= 0 && minutes % 60 <= 10
}


/**
 *
 */
fun getImageAsBase64(imagePath: File?): String? {
    Log.d("XXXimagexxxxx", "getImageAsBase64: $imagePath")
    imagePath?.let { imagePath ->
        val data: File? = saveBitmapToFile(imagePath)
        var bmp: Bitmap? = null
        var bos: ByteArrayOutputStream? = null
        var bt: ByteArray? = null
        var encodeString: String? = null
        try {
            bmp = BitmapFactory.decodeFile(data?.path)
            bmp?.let { bitMap ->
                bos = ByteArrayOutputStream()
                bos?.let { byteArray ->
                    bitMap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                    bt = byteArray.toByteArray()
                    encodeString = Base64.encodeToString(bt, Base64.DEFAULT)
                }
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return encodeString
    }
    return null
}


fun saveBitmapToFile(file: File): File? {
    return try {
        // BitmapFactory options to downsize the image
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        o.inSampleSize = 6
        // factor of downsizing the image
        var inputStream = FileInputStream(file)
        //Bitmap selectedBitmap = null;
        BitmapFactory.decodeStream(inputStream, null, o)
        inputStream.close()

        // The new size we want to scale to
        val REQUIRED_SIZE = 75

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2
        }
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        inputStream = FileInputStream(file)
        val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
        inputStream.close()

        // here i override the original image file
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        selectedBitmap?.let {
            it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        file
    } catch (e: Exception) {
        null
    }
}


fun setGlideImageUrl(
    originalImageView: ImageView, defaultImageView: ImageView, context: Context, imageUrl: String,
) {
    val requestOptions = RequestOptions()
    requestOptions.signature(ObjectKey(System.currentTimeMillis()))
    Glide.with(context).load(imageUrl).apply(requestOptions).into(originalImageView)
}


infix fun Any?.ifNull(block: () -> Unit) {
    if (this == null) block()
}

fun shareIntent(shareText: String, context: Context) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

fun getFileName(uri: Uri, context: Context): String? {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null)
    returnCursor.use { returnCursor ->
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            val img = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            if (nameIndex >= 0 && sizeIndex >= 0) {
                Log.d("TAG", "getFileName: ${returnCursor.getString(nameIndex)}")
                return returnCursor.getString(nameIndex)
            }
        }
    }
    return null
}


fun getPath(context: Context, uri: Uri?): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor = context.contentResolver.query(uri!!, projection, null, null, null) ?: return null
    val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    val s: String = cursor.getString(columnIndex)
    cursor.close()
    Log.d("TAG", "getPath: " + s)
    return s
}


fun fetchFilePath(context: Context, pdfName: String): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    return File.createTempFile(
        pdfName, /* prefix */
        ".pdf", /* suffix */
        storageDir      /* directory */
    )
}

fun showSingleButtonErrorDialog(context: Context, message: String) {
    val alertDialog: AlertDialog = context.let {
        val builder = AlertDialog.Builder(it)
        builder.apply {
            setPositiveButton(R.string.ok) { dialog, id ->
                setCancelable(true)
            }
            setMessage(message)
        }
        builder.create()
    }
    alertDialog.show()
}

fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir      /* directory */
    )
    // Save a file: path for use with ACTION_VIEW intents
    return image
}


/*fun getFilePathFromURI(contentUri: Uri?,context: Context): String? {
    //copy file and send new file path
    val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + VIDEO_DIRECTORY)
    // have the object build the directory structure, if needed.
    if (!wallpaperDirectory.exists()) {
        wallpaperDirectory.mkdirs()
    }
    val copyFile = File(
        wallpaperDirectory.toString() + File.separator + Calendar.getInstance().timeInMillis + ".mp4"
    )
    // create folder if not exists
    copy(context, contentUri, copyFile)
    Log.d("TAG", "getFilePathFromURI: ${copyFile.length()}")
    return copyFile.absolutePath
}*/


fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
    try {
        val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
        val outputStream: OutputStream = FileOutputStream(dstFile)
        copystream(inputStream, outputStream)
        inputStream.close()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


@Throws(java.lang.Exception::class, IOException::class)
fun copystream(input: InputStream?, output: OutputStream?): Int {
    val buffer = ByteArray(BUFFER_SIZE)
    val inFile = BufferedInputStream(input, BUFFER_SIZE)
    val out = BufferedOutputStream(output, BUFFER_SIZE)
    var count = 0
    var n = 0
    try {
        while (inFile.read(buffer, 0, BUFFER_SIZE).also { n = it } != -1) {
            out.write(buffer, 0, n)
            count += n
        }
        out.flush()
    } finally {
        try {
            out.close()
        } catch (e: IOException) {
            Log.e(e.message, e.toString())
        }
        try {
            inFile.close()
        } catch (e: IOException) {
            Log.e(e.message, e.toString())
        }
    }
    return count
}


fun calculateFileSize(file: File, maxSize: Int): Boolean {
    val maxFileSize = 150 * 1024 * 1024
    val l: Long = file.length()
    val fileSize = l.toString()
    val finalFileSize = fileSize.toInt()
    Log.d("TAG", "calculateFileSize: $finalFileSize")
    Log.d("TAG", "calculateFileSize: ${finalFileSize >= maxSize}")
    return finalFileSize <= maxSize

}


fun fetchVideoFilePath(context: Context, pdfName: String): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    return File.createTempFile(
        pdfName, /* prefix */
        ".mp4", /* suffix */
        storageDir      /* directory */
    )
}

var BUFFER_SIZE = 1024 * 2


fun contactPhoneIntent(context: Context, number: String) {
    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
    context.startActivity(intent)
}

fun contactEmailIntent(context: Context, receiverMaildId: String) {
    val emailIntent = Intent(
        Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", receiverMaildId, null
        )
    )
    context.startActivity(Intent.createChooser(emailIntent, null))
}

fun openUrl(activity: Activity, url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    activity.startActivity(browserIntent)
}

fun networkConnectionErrorDialog(context: Context) {
    showSingleButtonErrorDialog(context, context.getString(R.string.network_connection_error))
}

fun downloadManager(context: Context, url: String, fileName: String, downloadManager: DownloadManager): Long {
    val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
    val token = StorePreferences(context).token
    request.addRequestHeader("Authorization", token)
    request.setTitle(fileName).setDescription("File is downloading...").setDestinationInExternalFilesDir(
        context, Environment.DIRECTORY_DOWNLOADS, "Kapil Guru Study Material"
    ).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    //Enqueue the download.The download will start automatically once the download manager is ready
    // to execute it and connectivity is available.
    return downloadManager.enqueue(request)
}