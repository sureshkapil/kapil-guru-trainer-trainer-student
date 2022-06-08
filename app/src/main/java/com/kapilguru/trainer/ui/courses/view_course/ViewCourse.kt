package com.kapilguru.trainer.ui.courses.view_course

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.courseDetails.model.EnrolledCourseResponse
import com.kapilguru.student.courseDetails.review.model.StudentReviewedData
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityViewCourseBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.view_course.courseSyllabus.CourseSyllabusActivity
import com.kapilguru.trainer.ui.courses.view_course.view_model.CourseDetailsModelFactory
import com.kapilguru.trainer.ui.courses.view_course.view_model.CourseDetailsViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class ViewCourse : BaseActivity() {

    lateinit var viewModel: CourseDetailsViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var dataBinding: ActivityViewCourseBinding
    var courseId: String? = ""
    var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_course)
        viewModel = ViewModelProvider(this, CourseDetailsModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(CourseDetailsViewModel::class.java)
        dataBinding.model = viewModel
        viewModel.courseId.value = intent.getStringExtra(PARAM_COURSE_ID)

        customActionBarSetUp()
        dataBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        clickListeners()
        viewModelObservers()
        setRating()
        setCustomActionBarListener()

    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, dataBinding.customActionBar, getString(R.string.view_course))
    }

    private fun clickListeners() {
        dataBinding.upcomingBatches.setOnClickListener {
            navigateToBatches()
        }

        dataBinding.courseSyllabus.setOnClickListener {
            startActivity(Intent(this, CourseSyllabusActivity::class.java).apply {
                putExtra(PARAM_COURSE_SYLLABUS, viewModel.course.value)
            })
        }


        dataBinding.courseDescription.setOnClickListener {
            startActivity(Intent(this, CourseDescriptionActivity::class.java).apply {
                putExtra(PARAM_COURSE_DESCRIPTION, viewModel.course.value)
            })
        }

        dataBinding.aboutTrainer.setOnClickListener {
            naviagteToAboutTrainer()
        }

        dataBinding.tainerValue.setOnClickListener {
            naviagteToAboutTrainer()
        }

        dataBinding.review.setOnClickListener {
          /*  startActivity(Intent(this, ReviewActivity::class.java).apply {
                putExtra("PARAM_COURSE_ID", courseId)
            })*/
        }

        dataBinding.btnEnrollOrAddBatch.setOnClickListener {
            navigateToBatches()
        }

        /*dataBinding.certificate.setOnClickListener {
            startActivity(Intent(this, CertificateActivity::class.java))
        }*/

        dataBinding.btnContactTrainer.setOnClickListener {
            onContactTrainerClick()
        }
    }


    private fun onContactTrainerClick() {

    }


    private fun askForphonePermission() {
        Dexter.withContext(this).withPermissions(Manifest.permission.CALL_PHONE).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                p0?.let { multiplePermissionsReport ->
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        contactTrainerDialog()
                    } else {
                        showAppToast(this@ViewCourse, "Requires Camera Permission")
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                showAppToast(this@ViewCourse, "Please grant phone call Permission")
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }).check()
    }

    private fun contactTrainerDialog() {
       /* val fm: FragmentManager = supportFragmentManager
        val contactTrainerDialogFragment = ContactTrainerDialogFragment()
        val bundle = Bundle()
        bundle.putString(PARAM_CONTACT_TRAINER_NUMBER, viewModel.course.value?.contact)
        bundle.putString(PARAM_CONTACT_TRAINER_NAME, viewModel.course.value?.trainerName)
        contactTrainerDialogFragment.arguments = bundle
        contactTrainerDialogFragment.show(fm, "contact_trainer_dialog")*/
    }

    private fun naviagteToAboutTrainer() {
     /*   startActivity(Intent(this, AboutTrainer::class.java).apply {
            putExtra("PARAM_ABOUT_TRAINER", viewModel.course.value)
        })*/
    }

    private fun navigateToBatches() {
        /*startActivity(Intent(this, BatchList::class.java).apply {
            putParcelableArrayListExtra(PARAM_BATCHES_LIST, viewModel.batchesList)
            putExtra(PARAM_COURSE_ID, viewModel.courseId.value)
            putExtra(PARAM_IS_ENROLLED, viewModel.isCourseEnrolled.value)
        })*/
    }

    private fun viewModelObservers() {
        fetchCourseDetails()
        viewModel.resultDat.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    setCourseInfo(it.data?.allData?.course)
                    setBatchsInfo(it.data?.allData?.batches)
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    if (it.data?.status != 200) {
//                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
//                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }
        })


        viewModel.enrolledCourseResponse.observe(this, Observer { it ->
            when (it.status) {
                Status.LOADING -> {

                }

                Status.SUCCESS -> {
                    val enrolledCourse: EnrolledCourseResponse? = it.data
                    enrolledCourse?.let { response ->
                        viewModel.alreadyPurchasedData = response.data?.filter { it ->
                            it.studentId == userId && it.courseId == courseId?.toInt()
                        }
                        viewModel.alreadyPurchasedData?.let {
                            if (it.isNotEmpty()) {
                                viewModel.isCourseEnrolled.value = true
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    if (it.code == 401) {
                        sessionLogoutFragment()
                    } else {
                        showSingleButtonErrorDialog(this, getString(R.string.try_again))
                    }
                }
            }

        })


        viewModel.contactTrainerResponseAPi.observe(this, Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
//                    contactTrainerDialog()
                    askForphonePermission()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }

            }

        })

        viewModel.courseId.value?.let {
            viewModel.getStudentsReviewsList(it)
        }

        viewModel.studentListReviewResponse.observe(this, Observer {

            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    val response = it.data?.data
                    response?.let { it ->
                        viewModel.studentListReviewResponseData = it
                        viewModel.calculateRating(it)
                        setNumberOfRatedStudents(it)
                    }

                }
                Status.ERROR -> {

                }
            }
        })

    }

    private fun setNumberOfRatedStudents(it: List<StudentReviewedData>) {
        viewModel.studentRatingCount.observe(this, Observer { count ->
            count?.let {
                if (count == 1) {
                    dataBinding.studentNUmber.text = "${count} Student"
                } else {
                    dataBinding.studentNUmber.text = "${count} Students"
                }
            } ?: run {
                dataBinding.studentNUmber.text = "${count} Student"
            }

        })
    }

    private fun sessionLogoutFragment() {
      /*  val fm: FragmentManager = supportFragmentManager
        val sessionLogOutDialogFragment = SessionLogOutDialogFragment()
        sessionLogOutDialogFragment.show(fm, "session_logout_dialog")*/
    }

    private fun fetchCourseDetails() {
    /*    courseId = intent.getStringExtra(PARAM_COURSE_ID)
        userId = StorePreferences(this).studentId
        viewModel.getCourseDetails(courseId.toString(), userId.toString())*/
    }

    private fun setBatchsInfo(batches: List<BatchesItem>?) {
        batches?.let {
            viewModel.batchesList = it as ArrayList<BatchesItem>
            dataBinding.model = viewModel
        }
    }

    private fun setCourseInfo(course: Course?) {
       /* course?.let {
            dataBinding.language.text_value.text = getSelectedLanguagesString(application, course.language?.fromBase64()!!)
            viewModel.course.value = course
        }*/
    }

    private fun setRating() {
        viewModel.rating.observe(this, Observer { rating ->
            rating?.let {
                dataBinding.ratingBar.rating = it
            } ?: run {
                dataBinding.ratingBar.rating = 0.0f
            }
        })
    }

    private fun customActionBarSetUp() {
//        setActionbarBackListener(this, dataBinding.customActionBar.root, getString(R.string.course_details))
    }

    companion object {
        fun reLaunchActivityToCheckRegisterStatus(context: Context) {
            val intent = Intent(context, ViewCourse::class.java)
            context.startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        fetchCourseDetails()
    }

}