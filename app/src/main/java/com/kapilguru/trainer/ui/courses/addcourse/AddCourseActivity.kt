package com.kapilguru.trainer.ui.courses.addcourse

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityAddCourseBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.addcourse.models.AddCourseRequest
import com.kapilguru.trainer.ui.courses.addcourse.models.CoursePdfResponseApi
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.courses.addcourse.viewModel.AddCourseViewModel
import com.kapilguru.trainer.ui.courses.addcourse.viewModel.AddCourseViewModelFactory
import com.kapilguru.trainer.ui.courses.courses_list.CourseActivity
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.activity_add_course.*
import java.io.File
import java.util.*
class AddCourseActivity : BaseActivity() {
    lateinit var courseListInfo: ArrayList<CourseResponse>
    lateinit var addCourseViewModel: AddCourseViewModel
    lateinit var addCourseActivityBinding: ActivityAddCourseBinding
    lateinit var addCoursePageAdapter: AddCoursePageAdapter
    private val TAG = "AddCourseActivity"
    lateinit var dialog: CustomProgressDialog
    lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addCourseActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_course)
        this.setActionbarBackListener(this, addCourseActivityBinding.actionbar, getString(R.string.add_course))
        setUpViewModel()
        fragmentManager = supportFragmentManager
        courseListInfo = intent.getParcelableArrayListExtra<CourseResponse>(COURSE_INFO_PARAM) as ArrayList<CourseResponse>
        dialog = CustomProgressDialog(this)
        // sets up data if user is from Edit action
        onEditIntentAction()
        stepperLabels()
        setClickListeners()
        viewModelObservers()
    }

    private fun stepperLabels() {
        val descriptionData = arrayOf("Course\nDetails", "Course\nImage", "Demo\nVideo", "Lecture\nSyllabus", "Trainer\nInfo")
        addCourseActivityBinding.stepperStateProgressBar.setStateDescriptionData(descriptionData)
    }

    private fun setUpViewModel() {
        addCourseViewModel = ViewModelProvider(this, AddCourseViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(AddCourseViewModel::class.java)
        addCourseActivityBinding.addCourseViewModel = addCourseViewModel
        addCourseActivityBinding.lifecycleOwner = this
    }

    private fun onEditIntentAction() {
        if (intent.getBooleanExtra(IS_FROM_EDIT_PARAM, false)) {
            val courseId = intent.getIntExtra(COURSE_ID_PARAM, -1)
            if (courseId >= 0) {
                addCourseViewModel.courseId.value = courseId
                addCourseViewModel.isEdit = true
                addCourseViewModel.fetchCourseDetails()
            }
        } else {
            setPaginationAdapter(fragmentManager, courseListInfo)
        }
    }

    private fun setClickListeners() {
        addCourseActivityBinding.nextPager.setOnClickListener {
            if (addCourseActivityBinding.viewPager.currentItem <= 4) {
                when (addCourseViewModel.currentIndex.value) {
                    0 -> {
                            getCoursePageOneData()
                            validations(addCourseViewModel.addCourseRequest)
                    }
                    1 -> {
                        val addCourseImageFragment = supportFragmentManager.findFragmentByTag("f1")
                        val imageAsBase64String: String? = (addCourseImageFragment as AddCourseImageFragment).getImageAsBase64()
                            newuploadBase64ImageToAPI(imageAsBase64String)
                    }
                    2 -> {
//                        navigateToNextFragment()
                        val addCourseImageFragment = supportFragmentManager.findFragmentByTag("f2")
                        val uploadedVideoFilePath: File? = (addCourseImageFragment as AddCourseDemoVideoFragment).getUploadVideoPath()
                            uploadVideo(uploadedVideoFilePath)
                    }

                    3 -> {
                        val addCourseLectureSyllabusFragment =
                            supportFragmentManager.findFragmentByTag("f3")

                        when (addCourseViewModel.isPdfSelected) {

                            true -> if (addCourseViewModel.isPdfUploaded) {
                                val syllabus = (addCourseLectureSyllabusFragment as AddCourseLectureSyllabusFragment).getBase64File()
                                syllabus.first?.let { it ->
                                    addCourseViewModel.pdfName.value = syllabus.second!!
                                    addCourseViewModel.addCourseRequest.syllabusType = "pdf"
                                    uploadPdfToApi(it, syllabus.second!!)
                                } ?: kotlin.run {
                                    Toast.makeText(this, "Please upload Pdf or add syllabus", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                // if from edit mode then this value   filled earlier if not show error
                                if(isSyllabusAttached()) {
                                    Toast.makeText(this, "Please upload Pdf or add syllabus", Toast.LENGTH_LONG).show()
                                } else {
                                    navigateToNextFragment()
                                }

                            }

                            false -> {
                                val syllabus: String? =
                                    (addCourseLectureSyllabusFragment as AddCourseLectureSyllabusFragment).getEntireSyllabusAsBase64()
                                syllabus?.let {
                                    addCourseViewModel.addCourseRequest.syllabusTextContent = syllabus
                                    addCourseViewModel.addCourseRequest.syllabusType = "json"
                                    navigateToNextFragment()
                                } ?: kotlin.run {
                                    Toast.makeText(this, "Please add syllabus or upload pdf", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }

                    4 -> {
                        val myFragment = supportFragmentManager.findFragmentByTag("f4")
                        val aboutTrainer =
                            (myFragment as AddCourseTrainerInformationFragment).getAboutTrainer()
                        addCourseViewModel.addCourseRequest.aboutTrainer = aboutTrainer.toBase64()
                        addCourseViewModel.selectedLanguagesIds.value?.let {
                            addCourseViewModel.addCourseRequest.language = it.toBase64()
                        }
                        Log.d(TAG, "clickListerners: ${addCourseViewModel.addCourseRequest}")
                        validateTrainerDetails(addCourseViewModel.addCourseRequest)
                        return@setOnClickListener
                    }

                }

            }

        }

        addCourseActivityBinding.previousPager.setOnClickListener {
            if (addCourseActivityBinding.viewPager.currentItem <= 4) {
                if (addCourseActivityBinding.viewPager.currentItem == 0) {
                    return@setOnClickListener
                } else {
                    addCourseViewModel.currentIndex.value =
                        addCourseViewModel.currentIndex.value as Int - 1
                    addCourseActivityBinding.viewPager.setCurrentItem(
                        addCourseViewModel.currentIndex.value as Int,
                        true
                    )
                    changeStepperState(addCourseViewModel.currentIndex.value as Int)
                }
            }
        }
    }

    private fun getCoursePageOneData() {
        val myFragment =
            supportFragmentManager.findFragmentByTag("f" + viewPager.currentItem)
        val courseDescription =
            (myFragment as AddCourseTitleAndDescriptionFragment).getDataFromRichTextEditor()
        addCourseViewModel.addCourseRequest.description =
            courseDescription.toBase64()
        addCourseViewModel.addCourseRequest.trainerID =
            StorePreferences(AddCourseActivity@ this).userId.toString()
    }

    private fun uploadPdfToApi(it: File, pdfName: String) {
//        Log.d(TAG, "uploadPdfToApi: ${pdfName}")
        val pref = StorePreferences(application)
        addCourseViewModel.uploadPDF(file = it, trainerId = pref.userId.toString(), courseId = addCourseViewModel.courseId.value.toString(), pdfName = pdfName)
    }


    private fun newuploadBase64ImageToAPI(imageAsBase64String: String?) {
        imageAsBase64String?.let {
            if (addCourseViewModel.imageUpdated) {
                updateUploadedImageToApi(it)
            } else {
                navigateToNextFragment()
            }
        } ?: run {
            imageValidations(addCourseViewModel.addCourseRequest)
        }
    }

    private fun updateUploadedImageToApi(it: String) {
        Log.d(TAG, "updateUploadedImageToApi: ${ addCourseViewModel.addCourseRequest}")
        addCourseViewModel.uploadCourseImage(
            UploadImageCourse(
                code = addCourseViewModel.addCourseRequest.code.toString(),
                baseImage = it,
                sourceType = "Course",
                sourceId = addCourseViewModel.courseId.value!!
            )
        )
    }

    private fun uploadVideo(path: File?) {
        path?.let{
            if(addCourseViewModel.isVideoUpdated) {
                val pref = StorePreferences(application)
                addCourseViewModel.uploadVideo(file = path,
                    trainerId = pref.userId.toString(),
                    sourceId = addCourseViewModel.courseId.value.toString(),
                    code = addCourseViewModel.addCourseRequest.code!!,
                    videoType = "Course")
            } else {
                navigateToNextFragment()
            }
        }?: run {
            navigateToNextFragment()
        }
    }

    private fun setPaginationAdapter(
        fragmentManager: FragmentManager,
        courseListInfo: ArrayList<CourseResponse>
    ) {
        addCoursePageAdapter = AddCoursePageAdapter(fragmentManager, lifecycle, courseListInfo)

        addCourseActivityBinding.viewPager.offscreenPageLimit = 5
        addCourseActivityBinding.viewPager.adapter = addCoursePageAdapter


        // Disable Scrolling for View Pager
        addCourseActivityBinding.viewPager.isUserInputEnabled = false
    }

    private fun validations(addCourseRequest: AddCourseRequest) {
        when {
            addCourseRequest.courseTitle.isNullOrBlank() -> {
                showErrorMessage("please fill the Course Title")
            }
            addCourseRequest.courseSubTitle.isNullOrBlank() -> {
                showErrorMessage("please fill courseSubTitle")
            }
            addCourseRequest.categoryID.isNullOrBlank() -> {
                showErrorMessage("please fill category")
            }
            addCourseRequest.fee.isNullOrBlank() -> {
                showErrorMessage("please fill the Price")
            }
            isPriceInRange(addCourseRequest.fee!!.toString()) -> {
                    showErrorMessage("Please ensure Price should be in between 400 Rupees to 1,00,000 Rupees")
            }
            addCourseRequest.actualFee.isNullOrBlank() -> {
                showErrorMessage("please fill the offer Price")
            }
            isPriceInRange(addCourseRequest.actualFee!!.toString()) -> {
                    showErrorMessage("Please ensure Price should be in between 400 Rupees to 1,00,000 Rupees")
            }
            actualPriceIsInLimits(addCourseRequest) -> {
                showErrorMessage("Discount Price can't be more than Price")
            }
            addCourseRequest.durationDays.isNullOrBlank() -> {
                showErrorMessage("please fill course Duration Days")
            }
            addCourseRequest.description.isNullOrBlank() -> {
                showErrorMessage("please fill the Description")
            }
            else -> {
                if(!addCourseViewModel.newisApiRequestMade) {
                    addCourseViewModel.saveAndPostTitleAndText()
                } else {
                    navigateToNextFragment()
                }
            }
        }
    }

    private fun actualPriceIsInLimits(addCourseRequest: AddCourseRequest) = addCourseRequest.actualFee!!.toInt() > addCourseRequest.fee!!.toInt()

    private fun isPriceInRange(price: String): Boolean = (price.toInt()<400 ||price.toInt()>100000)


    private fun imageValidations(addCourseRequest: AddCourseRequest) {
        when {
            addCourseRequest.courseImage.isNullOrBlank() -> {
                showErrorMessage("please upload course image")
            }
            addCourseRequest.courseImage.toString().contains("null", true) &&  !addCourseViewModel.imageUpdated -> {
                showErrorMessage("please upload course image")
            }
            else -> {
                if (addCourseViewModel.isEdit && !addCourseRequest.courseImage.isNullOrEmpty()) {
                    navigateToNextFragment()
                }
            }
        }
    }


    private fun validateTrainerDetails(addCourseRequest: AddCourseRequest) {
        when {
            addCourseRequest.trainerName.isNullOrBlank() -> {
                showErrorMessage("please fill the trainer name")
            }
            addCourseRequest.trainersYearOfExp.isNullOrBlank() -> {
                showErrorMessage("please fill Training yrs of Experience")
            }
            experienceValidation(addCourseRequest) -> {
                    showErrorMessage("Training Experience Can't exceed 50")
            }
            addCourseRequest.totalNoOfStudentsTrained.isNullOrBlank() -> {
                showErrorMessage("please fill Number Of students trained")
            }
            addCourseRequest.aboutTrainer.isNullOrBlank() -> {
                showErrorMessage("please fill info about trainer")
            }
            addCourseRequest.language.isNullOrBlank() -> {
                showErrorMessage("please select languages")
            }
            else -> {
                if (addCourseRequest.syllabusType == "pdf") {
                    addCourseViewModel.addCourseRequest.syllabusTextContent = null
                } else {
                    addCourseViewModel.addCourseRequest.syllabusAttachment = null
                }
                addCourseViewModel.updateCourse()
            }
        }
    }

    private fun experienceValidation(addCourseRequest: AddCourseRequest) = addCourseRequest.trainersYearOfExp!!.toInt() > 50

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun changeStepperState(currentPosition: Int) {
        when (currentPosition) {
            0 -> stepperStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
            1 -> stepperStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
            2 -> stepperStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
            3 -> stepperStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
            4 -> stepperStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE)
        }
    }

    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        setCancelable(true)
                    })

                setMessage(message)

            }

            // Create the AlertDialog
            builder.create()
        }
        alertDialog.show()
    }


    private fun viewModelObservers() {
        // add course Title response handled here
        addCourseViewModel.addCourseResponse.observe(this, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    apiResponse.data?.addCourseApiData?.let { it ->
                        addCourseViewModel.courseId.value = it.insertId
                        addCourseViewModel.newisApiRequestMade = true
                        addCourseViewModel.addCourseRequest.code = it.code
                    }
                    navigateToNextFragment()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
            }
        })

        // course syllabus and trainer Info updated here
        addCourseViewModel.updateCourseApi.observe(this, Observer { apiResponse ->

            when (apiResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    apiResponse.data?.updateCourseResponse?.let { it ->
                        navigateToMyCourseActivity()
                    }
                    Toast.makeText(this, "Success_123", Toast.LENGTH_LONG).show()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    Toast.makeText(this, "error", Toast.LENGTH_LONG).show()
                }
            }

        })

        // on edit get the course data
        addCourseViewModel.getCourseRequest.observe(this, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    apiResponse.data?.addCourseRequest?.let { it ->
                        addCourseViewModel.addCourseRequest = it[0]
                        addCourseViewModel.newisApiRequestMade = true
                        setPaginationAdapter(fragmentManager, courseListInfo)
                    }
                    Toast.makeText(this, "Success_123", Toast.LENGTH_LONG).show()

                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    Toast.makeText(this, "error", Toast.LENGTH_LONG).show()
                }


            }
        })

        // upload course Image
        addCourseViewModel.uploadImageCourseResponse.observe(this, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    addCourseViewModel.addCourseRequest.courseImage = addCourseViewModel.addCourseRequest.code.plus(".png")
                    addCourseViewModel.imageUpdated = false
                    navigateToNextFragment()
                    Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_LONG).show()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    Toast.makeText(this, "error", Toast.LENGTH_LONG).show()
                }

            }
        })

        // upload pdf handling
        addCourseViewModel.uploadPdfResponse.observe(this, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    addCourseViewModel.addCourseRequest.syllabusType = "pdf"
                    addCourseViewModel.addCourseRequest.syllabusAttachment = apiResponse.data?.commonResponse?.insertId
                    navigateToNextFragment()
                    Toast.makeText(this, "Pdf added", Toast.LENGTH_LONG).show()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }

            }
        })


        /// upload course Video
        addCourseViewModel.uploadVideoResponse.observe(this, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    addCourseViewModel.isVideoUpdated = false
                    addCourseViewModel.addCourseRequest.courseVideo = addCourseViewModel.addCourseRequest.code+".mp4"
                    navigateToNextFragment()
//                    Toast.makeText(this, "Pdf added", Toast.LENGTH_LONG).show()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    addCourseViewModel.isVideoUpdated = false
                    // show error message
                }

            }
        })

        // get pdf Data
        addCourseViewModel.getPdfResponse.observe(this, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    apiResponse.data?.data?.let {it->
                        if(!it.isNullOrEmpty()){
                    val firstElement: CoursePdfResponseApi = it[0]
                    addCourseViewModel.pdfName.value = firstElement.documentTitle.toString()
                    addCourseViewModel.downloadPdfUrl.value = firstElement.filename
                     Toast.makeText(this, "Pdf is attached", Toast.LENGTH_LONG).show()
                        }
                    }
                    dialog.dismissLoadingDialog()

                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

    }

    private fun isSyllabusAttached():Boolean {
        return addCourseViewModel.addCourseRequest.syllabusType==null
    }

    private fun navigateToMyCourseActivity() {
        val intent = Intent(this, CourseActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


    private fun navigateToNextFragment() {
        addCourseViewModel.currentIndex.value =
            addCourseViewModel.currentIndex.value as Int + 1
        addCourseActivityBinding.viewPager.setCurrentItem(
            addCourseViewModel.currentIndex.value as Int,
            true
        )
        //Change Stepper
        changeStepperState(addCourseViewModel.currentIndex.value as Int)
    }

}