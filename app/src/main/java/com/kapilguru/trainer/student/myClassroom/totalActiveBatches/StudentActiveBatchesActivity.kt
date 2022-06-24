package com.kapilguru.trainer.student.myClassroom.totalActiveBatches

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityStudentActiveBatchesBinding
import com.kapilguru.trainer.student.myClassRoomDetails.StudentMyClassDetailsActivity
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentActiveBatchData

class StudentActiveBatchesActivity : BaseActivity(), StudentActiveBatchesAdapter.StudentBatchClickListener {
    lateinit var binding: ActivityStudentActiveBatchesBinding
    lateinit var viewModel: StudentActiveBatchesViewModel
    lateinit var adapter: StudentActiveBatchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_active_batches)
        viewModel = ViewModelProvider(this).get(StudentActiveBatchesViewModel::class.java)
        getAndSetIntentData()
        setActionBar()
        setAdapter()
    }

    private fun getAndSetIntentData() {
        viewModel.batchList = intent.getParcelableArrayListExtra<StudentActiveBatchData>(batch_list) as ArrayList<StudentActiveBatchData>
        viewModel.courseName = intent.getStringExtra(course_name)!!
    }

    private fun setActionBar() {
        val title = viewModel.courseName + " Batches"
        setActionbarBackListener(this, binding.customActionBar.rootView, title)
    }

    private fun setAdapter() {
        adapter = StudentActiveBatchesAdapter(this)
        binding.rvBatch.adapter = adapter
        adapter.setData(viewModel.batchList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val batch_list = "BATCH_LIST_PARAM"
        const val course_name = "COURSE_NAME"
    }

    override fun onBatchClicked(batchData: StudentActiveBatchData) {

    }

    override fun onOverViewClicked(batchData: StudentActiveBatchData) {
        StudentMyClassDetailsActivity.launchActivity(batchData.batchId.toString(), this, 0)
    }

    override fun onStudyMaterialClicked(batchData: StudentActiveBatchData) {
        StudentMyClassDetailsActivity.launchActivity(batchData.batchId.toString(), this, 2)
    }

    override fun onExamClicked(batchData: StudentActiveBatchData) {
        StudentMyClassDetailsActivity.launchActivity(batchData.batchId.toString(), this, 3)
    }

    override fun onCompletionRequestClicked(batchData: StudentActiveBatchData) {

    }
}