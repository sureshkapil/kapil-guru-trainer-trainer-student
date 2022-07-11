package com.kapilguru.trainer.addStudent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentAddOnlineStudentBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.showAppToast
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListResponse
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class AddOnlineStudentFragment : Fragment() {

    lateinit var binding: FragmentAddOnlineStudentBinding
    val viewModel: AddStudentViewModel by viewModels({ requireActivity() })
    lateinit var progressDialog: CustomProgressDialog
    lateinit var courseSpinnerAdapter: CourseSpinnerAdapter
    lateinit var batchSpinnerAdapter: BatchSpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_online_student, container, false)
        binding = FragmentAddOnlineStudentBinding.inflate(inflater, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        clickListeners()
        observeViewModelData()
    }

    fun clickListeners() {

        binding.aCSpinnerCourse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    viewModel.courseId = viewModel.courseListApi[position].courseId!!
                    getCourseDetails(viewModel.courseId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.aCSpinnerBatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.batchId = viewModel.batchListApi[position].batchId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


        binding.addButton.setOnClickListener {
            if (viewModel.validateUserData()) {
                viewModel.checkStudent()
            }
        }
    }

    private fun observeViewModelData() {
        viewModel.getCourseList()
        viewModel.checkStudentResponse.observe(this.viewLifecycleOwner, Observer { checkStudentResponse ->
            when (checkStudentResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    checkStudentResponse?.data?.data?.let { it ->
                        viewModel.studentId.value = it.studentId
                        shouldAddStudent()
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.courseList.observe(this.viewLifecycleOwner, { courseList ->
            when (courseList.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    courseList?.data?.courseResponse?.let { it ->
                        viewModel.courseListApi = it as ArrayList<CourseResponse>
                        viewModel.courseListApi.add(0, CourseResponse(courseTitle = "Select Course"))
                        setCourseAdapters()
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.batchList.observe(this.viewLifecycleOwner, { batchList ->
            when (batchList.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    batchList?.data?.batchListResponse?.let { it ->
                        viewModel.batchListApi = it as ArrayList<BatchListResponse>
                        setBatchSpinnerAdapter()
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.errorText.observe(this.viewLifecycleOwner, { errorText ->
            showToast(errorText)
        })
    }

    private fun showToast(errorText: String) {
        showAppToast(this.requireContext(), errorText)
    }

    private fun shouldAddStudent() {
        if(viewModel.studentId.value==0) {
            viewModel.addOnlineStudent()
        }
    }


    fun setCourseAdapters() {
        courseSpinnerAdapter = CourseSpinnerAdapter(this.requireContext(), viewModel.courseListApi)
        binding.aCSpinnerCourse.adapter = courseSpinnerAdapter
        courseSpinnerAdapter.notifyDataSetChanged()
    }

    fun setBatchSpinnerAdapter() {
        batchSpinnerAdapter = BatchSpinnerAdapter(this.requireContext(), viewModel.batchListApi)
        binding.aCSpinnerBatch.adapter = batchSpinnerAdapter
        batchSpinnerAdapter.notifyDataSetChanged()
    }

    fun getCourseDetails(id: Int) {
        viewModel.getBatchList(id.toString())
    }


    companion object {
        @JvmStatic
        fun newInstance() = AddOnlineStudentFragment().apply {}
    }
}