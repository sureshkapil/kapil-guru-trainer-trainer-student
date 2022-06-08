package com.kapilguru.trainer.studentsList.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityStudentListBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.studentsList.CourseListRecyclerAdapter
import com.kapilguru.trainer.studentsList.StudentListActivityTOAdapters
import com.kapilguru.trainer.studentsList.model.StudentDetails
import com.kapilguru.trainer.studentsList.viewModel.StudentListViewModel
import com.kapilguru.trainer.studentsList.viewModel.StudentListViewModelFactory

class StudentList : BaseActivity(), StudentListActivityTOAdapters {


    lateinit var viewModel: StudentListViewModel
    lateinit var studentListBinding: ActivityStudentListBinding
    lateinit var adapter: CourseListRecyclerAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        studentListBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_list)
        viewModel = ViewModelProvider(this, StudentListViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(StudentListViewModel::class.java)
        studentListBinding.viewModel = viewModel
        studentListBinding.lifecycleOwner = this
        this.setActionbarBackListener(this, studentListBinding.actionbar, getString(R.string.my_students))
        dialog = CustomProgressDialog(this)
        setAdapter()
        apiCallToFetchStudents()
        observeViewModelData()

    }

    private fun apiCallToFetchStudents() {
        when(intent.extras?.getString(PARAM_IS_FROM,PARAM_IS_FROM_DASHBOARD)) {
            PARAM_IS_FROM_DASHBOARD -> viewModel.fetchStudentListApi(StorePreferences(this).trainerId.toString())
            PARAM_IS_FROM_COURSE -> {
                val courseId = intent.extras?.getString(PARAM_COURSE_ID, null)
                courseId?.let { it ->
                    viewModel.fetchStudentListForCourseApi(it)
                }
            }
            PARAM_IS_FROM_BATCH -> {
                val batchId = intent.extras?.getString(PARAM_BATCH_ID, null)
                batchId?.let { it ->
                    viewModel.fetchStudentListForBatchApi(it)
                }
            }
        }
    }

    private fun setAdapter() {
        adapter = CourseListRecyclerAdapter(this as StudentListActivityTOAdapters)
        studentListBinding.studentListRecycler.adapter = adapter
    }

    private fun observeViewModelData() {

        viewModel.resultDat.observe(this, Observer {

            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val response = it.data?.studentDetails
                    adapter.setData(response as ArrayList<StudentDetails>)
                    viewModel.studentCount.value = response.size.toString()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

    }

    override fun getStudentId(studentId: Int) {
        startActivity(Intent(this, RaiseComplaint::class.java).putExtra(COMPLAINT_STUDENT_ID, studentId))
    }
}