package com.kapilguru.trainer.studentExamBatchResult

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityViewStudentReportBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import java.lang.reflect.Type

class ViewStudentReport : BaseActivity() {

    lateinit var viewModel: StudentExamBatchResultViewModel
    lateinit var binding: ActivityViewStudentReportBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var studentName: String
    lateinit var studentCode: String
    var studentReportRequest: StudentReportRequest?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_student_report)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, StudentExamBatchResultViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(StudentExamBatchResultViewModel::class.java)
        progressDialog = CustomProgressDialog(this)
        binding.viewModel = viewModel
        setSupportActionBar()
        observeViewModel()
        clickListerners()
    }

    private fun clickListerners() {
        binding.tvViewAnsSheet.setOnClickListener {
            val intent = Intent(this, ViewAnswerSheetActivity::class.java)
            intent.putExtra(Batch_STUDENT_Reports_Request_PARAM,studentReportRequest)
            intent.putExtra(STUDENT_NAME_PARAM,studentName)
            intent.putExtra(STUDENT_CODE_PARAM,studentCode)
            startActivity(intent)
        }
    }


    private fun setSupportActionBar() {
        setActionbarBackListener(this,binding.actionbar,getString(R.string.exam_results))
    }

    private fun observeViewModel() {
        studentReportRequest = intent?.getParcelableExtra<StudentReportRequest>(Batch_STUDENT_Reports_Request_PARAM)
        studentReportRequest?.let {
            viewModel.getStudentsReports(it)
        }

        viewModel.batchExamStudentResultApi.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    val studentsInfo = response.data?.batchExamStudentResultResponse?.studentResults
                    val gson = Gson()
                    studentsInfo?.let { it ->
                        val studentResultType: Type =
                            object : TypeToken<StudentResult?>() {}.type
                        val studentResult: StudentResult = gson.fromJson(studentsInfo.toString(), studentResultType)
                        studentResult.let { studentResult ->
                            viewModel.studentResult.value = studentResult
                            studentName = studentResult.studentName
                            studentCode = studentResult.studentCode
                        }
                        showPieChart()
                    }
                    var studentInfoType = studentsInfo
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    if (response.data?.status != 200) {

                    } else {

                    }
                }
            }
        })
    }


    private fun showPieChart() {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        //initializing data
        val typeAmountMap: HashMap<String, Int> = HashMap()
        typeAmountMap["Correct"] =viewModel.studentResult.value!!.correctAnswers
        typeAmountMap["Wrong"] = viewModel.studentResult.value!!.incorrectAnswers
        typeAmountMap["Unattempted"] =viewModel.studentResult.value!!.unattemptedQuestions

        //initializing colors for the entries
        val colors: ArrayList<Int> = arrayListOf()
        colors.add(Color.parseColor("#18c55c"))
        colors.add(Color.parseColor("#ff4900"))
        colors.add(Color.parseColor("#868686"))

        //input data and fit data into pie chart entry

        for (type: String in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type));
        }

        //collecting the entries with label name
        val pieDataSet: PieDataSet = PieDataSet(pieEntries, "label");
        //setting text size of the value
        pieDataSet.valueTextSize = 12f;
        //providing color list for coloring different entries
        pieDataSet.colors = colors;
        //grouping the data set from entry to chart
        val pieData: PieData = PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        binding.pieChartView.data = pieData
        binding.pieChartView.invalidate()

    }

}