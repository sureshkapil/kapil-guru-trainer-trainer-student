package com.kapilguru.trainer.exams.assignExamToBatch

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapilguru.trainer.*
import com.kapilguru.trainer.cutomDialog.ClassDurationDialog
import com.kapilguru.trainer.cutomDialog.DurationModel
import com.kapilguru.trainer.databinding.ActivityAssignExamToBatchBinding
import com.kapilguru.trainer.exams.assignExamToBatch.model.BatchByCourseResData
import com.kapilguru.trainer.exams.assignExamToBatch.viewModel.AssignExamToBatchViewModel
import com.kapilguru.trainer.exams.assignExamToBatch.viewModel.AssignExamToBatchViewModelFactory
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaper
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import com.kapilguru.trainer.ui.home.HomeActivity
import java.text.SimpleDateFormat
import java.util.*

class AssignExamToBatchActivity : BaseActivity() {
    private val TAG = "AssignExamToBatchActivity"
    lateinit var binding: ActivityAssignExamToBatchBinding
    lateinit var viewModel: AssignExamToBatchViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: BatchByCourseListAdapter
    var examDateAndTime: Calendar = Calendar.getInstance()

    private val durationList = ArrayList<DurationModel>().apply {
        add(DurationModel("30 Minutes", 30, false))
        add(DurationModel("1 Hour", 60, false))
        add(DurationModel("1 Hour 30 Minutes", 90, false))
        add(DurationModel("2 Hours", 120, false))
        add(DurationModel("2 Hours 30 Minutes", 150, false))
        add(DurationModel("3 Hours", 180, false))
    }
    var previousSelected = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assign_exam_to_batch)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, AssignExamToBatchViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(AssignExamToBatchViewModel::class.java)
        progressDialog = CustomProgressDialog(this)
        getIntentData()
        binding.viewModel = viewModel
        setCustomActionBarListener()
        setAdapter()
        observeViewModelData()
        setClickListeners()
        viewModel.getActiveBatchesByCourse()
    }

    private fun getIntentData() {
        viewModel.course = intent.getParcelableExtra(KEY_CORSE)!!
        viewModel.questionPaper = intent.getParcelableExtra(KEY_QUESTION_PAPER)!!
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.schedule_exams))
    }

    private fun setAdapter() {
        adapter = BatchByCourseListAdapter()
        binding.rvBatches.layoutManager = LinearLayoutManager(this)
        binding.rvBatches.adapter = adapter
    }

    private fun setClickListeners() {
        binding.tietExamDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.tietStartTime.setOnClickListener {
            showTimePickerDialog()
        }
        binding.tietClassDuartion.setOnClickListener {
            showExamDuration()
        }
        binding.btnSubmit.setOnClickListener {
            callAssignExamToBatchApi()
        }
    }

    private fun showDatePickerDialog() {
        val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            examDateAndTime.set(Calendar.YEAR, year)
            examDateAndTime.set(Calendar.MONTH, month)
            examDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            showSelectedDate()
        }
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this, dateListener, year, month, day)
        datePickerDialog.datePicker.minDate = calender.timeInMillis
        datePickerDialog.show()
    }

    private fun showSelectedDate() {
        val myFormat = "MM/dd/yy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.tietExamDate.setText(sdf.format(examDateAndTime.time))
    }

    private fun showTimePickerDialog() {
        val timeSelectListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            examDateAndTime.set(Calendar.HOUR, hourOfDay)
            examDateAndTime.set(Calendar.MINUTE, minute)
            showSelectedTime()
        }
        val calender = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR)
        val minute = calender.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(this, timeSelectListener, hour, minute, false)
        timePicker.show()
    }

    private fun showSelectedTime() {
        val dateFormat = SimpleDateFormat("hh:mm")
        val selectedTime = dateFormat.format(examDateAndTime.time)
        binding.tietStartTime.setText(selectedTime)
    }

    private fun showExamDuration() {
        ClassDurationDialog.newInstance(createData()).show(supportFragmentManager, "1")
        supportFragmentManager.setFragmentResultListener("abc", this) { key, bundle ->
            val result = bundle.getInt("resultKey")
            Log.d(TAG, "putStartTimeCalendar: $result")

            if (previousSelected >= 0) {
                durationList.apply {
                    this[previousSelected].isSelected = false
                }
            }
            durationList.apply {
                previousSelected = result
                this[result].isSelected = true
                binding.tietClassDuartion.setText(durationList[result].key)
            }
        }
    }

    private fun createData(): ArrayList<DurationModel> {
        return durationList
    }

    private fun callAssignExamToBatchApi() {
        setAssignExamToBatchReqData()
        viewModel.callAssignExamToBatchApi()
    }

    private fun setAssignExamToBatchReqData() {
        viewModel.assignExamToBatchApiReq.value?.trainerId = viewModel.course.trainerId.toString()
        viewModel.assignExamToBatchApiReq.value?.courseId = viewModel.course.courseId!!
        viewModel.assignExamToBatchApiReq.value?.questionPaperId = viewModel.questionPaper.questionPaperId
        viewModel.assignExamToBatchApiReq.value?.examdate = examDateAndTime.convertDateAndTimeToApiDataWithoutT()
        viewModel.assignExamToBatchApiReq.value?.batchId = adapter.getSelectedBatch().batchId
        viewModel.assignExamToBatchApiReq.value?.duration = durationList[previousSelected].value.toString()
    }

    private fun observeViewModelData() {
        observeBatchByCourseApiRes()
        observeAssignExamToBatchApiRes()
    }

    private fun observeBatchByCourseApiRes() {
        viewModel.batchByCourseApiResponse.observe(this, Observer { batchByCourseApiRes ->
            when (batchByCourseApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    batchByCourseApiRes.data?.let { batchByCourseRes ->
                        batchByCourseRes.batchesByCourseList?.let { batchList ->
                            setAdapterData(batchList)
                        }
                        progressDialog.dismissLoadingDialog()
                    }
                }
                Status.ERROR -> {
                    progressDialog.showLoadingDialog()
                }
            }
        })
    }

    private fun setAdapterData(batchList: ArrayList<BatchByCourseResData>) {
        adapter.setData(batchList)
    }

    private fun observeAssignExamToBatchApiRes() {
        viewModel.assignExamToBatchApiResponse.observe(this, Observer { assignExamToBatchApiRes ->
            when (assignExamToBatchApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    finishAffinity()
                    startActivity(Intent(this,HomeActivity::class.java))
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    companion object {
        val KEY_CORSE = "kEY_COURSE"
        val KEY_QUESTION_PAPER = "QUESTION_PAPER"

        fun launchActivity(course: CourseResponse, questionPaper: QuestionPaper, activity: Activity) {
            val intent = Intent(activity, AssignExamToBatchActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelable(KEY_CORSE, course)
                    putParcelable(KEY_QUESTION_PAPER, questionPaper)
                }
                putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }
}