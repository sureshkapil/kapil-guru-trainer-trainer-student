package com.kapilguru.trainer.studentExamBatchResult

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.trainer.Batch_STUDENT_Reports_Request_PARAM
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentVIewReportBinding
import com.kapilguru.trainer.network.Status
import java.lang.reflect.Type


/**
 * A simple [Fragment] subclass.
 * Use the [VIewReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewReportFragment : Fragment() {

    lateinit var binding: FragmentVIewReportBinding
    val viewModel: StudentExamBatchResultViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    private val TAG = "ViewReportFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVIewReportBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.activity
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        observeViewModel()
    }

    private fun showPieChart() {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        //initializing data
        val typeAmountMap: HashMap<String, Int> = HashMap()
        typeAmountMap["Correct"] = binding.viewModel!!.studentResult.value!!.correctAnswers
        typeAmountMap["Wrong"] = binding.viewModel!!.studentResult.value!!.incorrectAnswers
        typeAmountMap["Unattempted"] =binding.viewModel!!.studentResult.value!!.unattemptedQuestions

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

    private fun observeViewModel() {
        val studentReportRequest = activity?.intent?.getParcelableExtra<StudentReportRequest>(Batch_STUDENT_Reports_Request_PARAM)
        studentReportRequest?.let {
            viewModel.getStudentsReports(it)
        }

        viewModel.batchExamStudentResultApi.observe(this.viewLifecycleOwner, Observer { response ->
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
                            Log.d(TAG, "observeViewModel_123: $studentResult")
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


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewReportFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}