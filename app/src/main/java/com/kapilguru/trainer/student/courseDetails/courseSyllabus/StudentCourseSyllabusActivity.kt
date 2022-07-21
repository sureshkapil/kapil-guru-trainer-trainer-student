package com.kapilguru.trainer.student.courseDetails.courseSyllabus

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityStudentCourseSyllabusBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.StudentBaseActivity
import com.kapilguru.trainer.student.courseDetails.viewModel.StudentCourseDetailsModelFactory
import com.kapilguru.trainer.student.courseDetails.viewModel.StudentCourseDetailsViewModel
import com.kapilguru.trainer.ui.courses.view_course.Course
import com.kapilguru.trainer.ui.courses.view_course.PdfViewerActivity
import com.kapilguru.trainer.ui.courses.view_course.SyllabusAttachmentData
import org.json.JSONArray
import org.json.JSONObject


class StudentCourseSyllabusActivity : StudentBaseActivity() {

    private var downloadManager: DownloadManager? = null
    private var downLoadId: Long = 0
    lateinit var viewModel: StudentCourseDetailsViewModel
    lateinit var dataBinding: ActivityStudentCourseSyllabusBinding
    lateinit var progressDialog: CustomProgressDialog
    private var pdfData: SyllabusAttachmentData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, StudentCourseDetailsModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(StudentCourseDetailsViewModel::class.java)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_course_syllabus)
        setActionbarBackListener(this, dataBinding.customActionBar.root, getString(R.string.course_syllabus_title))
        progressDialog = CustomProgressDialog(this)
        initializeDownloadManager()
        getIntentData()
        setOnClickListener()
        observerViewModel()
    }

    private fun observerViewModel() {
        viewModel.syllabusResponse.observe(this, Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    pdfData = it.data?.data?.get(0)
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when (it.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            showSingleButtonErrorDialog(this, getString(R.string.network_connection_error))
                        }
                    }
                }
            }
        })
    }

    private fun setOnClickListener() {
        /*dataBinding.btnPdfDownload.setOnClickListener {
            pdfData?.let {
                onDownloadClicked(it.filename)
            }
        }*/

        dataBinding.btnPdfDownload.setOnClickListener {
            pdfData?.let { pdfDataNotNull ->
                val downloadUrl = BuildConfig.BASE_URL + PDF_URL + pdfDataNotNull.filename
                launchPdfActivity(downloadUrl)
            }
        }
    }

    private fun getIntentData() {
        val studentCourse: Course? = intent.getParcelableExtra(PARAM_COURSE_SYLLABUS)
        dataBinding.model = studentCourse

        studentCourse?.let {
            if (studentCourse.syllabusType.equals("pdf")) {

                // need to call api
                viewModel.getPdfAttachments(studentCourse.syllabusAttachment!!)
            } else if (studentCourse.syllabusType.equals("json")) {

                convertBase64ToReadableData(studentCourse.syllabusTextContent)
            }
        }

    }

    private fun initializeDownloadManager() {
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
    }

    private fun downloadFile(url: String, fileName: String) {
        val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
        val token = StorePreferences(this).token
        request.addRequestHeader("Authorization", token)
        request.setTitle(fileName).setDescription("File is downloading...").setDestinationInExternalFilesDir(
            this, Environment.DIRECTORY_DOWNLOADS, "Kapil Guru Study Material"
        ).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        //Enqueue the download.The download will start automatically once the download manager is ready
        // to execute it and connectivity is available.
        downLoadId = downloadManager!!.enqueue(request)
        getDownloadStatus(downLoadId)
    }


    private fun getDownloadStatus(downLoadId: Long) {
        val cursor: Cursor? = downloadManager!!.query(DownloadManager.Query().setFilterById(downLoadId))
        var statusString = ""
        if (cursor != null && cursor.moveToNext()) {
            val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            cursor.close()
            when (status) {
                DownloadManager.STATUS_FAILED -> {
                    statusString = "Download Failed"

                }
                DownloadManager.STATUS_PENDING, DownloadManager.STATUS_PAUSED -> {
                    statusString = "Download Pending"
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    statusString = "Download Success"
                }
                DownloadManager.STATUS_RUNNING -> {
                    statusString = "Download Running"
                }
            }
        }

        showAppToast(this, statusString)
    }


    private fun onDownloadClicked(fileName: String) {
        val downloadUrl = BuildConfig.BASE_URL + "courseFiles/files/" + fileName
        downloadFile(downloadUrl, fileName)
    }

    private fun convertBase64ToReadableData(syllabusTextContent: String?) {
        val data = syllabusTextContent?.fromBase64ToString()
        data?.let {
            val abc = JSONArray(it)
            for (i in 0 until abc.length()) {
                val jsonObject: JSONObject = abc.getJSONObject(i)
                setEditDynamicLayout(
                    jsonObject.getString("syllabusHeading"), jsonObject.getString("syllabusSubHeading")
                )
            }
        }
    }

    private fun setEditDynamicLayout(Heading: String, subHeading: String) {
        val vi = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = vi.inflate(R.layout.added_syllabus, null)
        v.findViewById<TextView>(R.id.syllabus_title).text = Heading
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            v.findViewById<TextView>(R.id.syllabus_content).text = Html.fromHtml(subHeading, Html.FROM_HTML_MODE_LEGACY)
        } else {
            v.findViewById<TextView>(R.id.syllabus_content).text = Html.fromHtml(subHeading)
        }

        dataBinding.jsonContentText.addView(v)
    }

    private fun launchPdfActivity(url: String) {
        val intent = Intent(this, PdfViewerActivity::class.java).putExtra(PDF_PARAM, url)
        startActivity(intent)
    }

    companion object {
        fun launchActivity(activity: Activity, course: Course?) {
            activity.startActivity(Intent(activity, StudentCourseSyllabusActivity::class.java).apply {
                putExtra(PARAM_COURSE_SYLLABUS, course)
            })
        }
    }
}