package com.kapilguru.trainer.myClassroom.totalActiveBatches

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.databinding.ActivityActiveBatchesBinding
import com.kapilguru.trainer.exams.ExamsActivity
import com.kapilguru.trainer.myClassRoomDetails.MyClassDetails

class ActiveBatchesActivity : BaseActivity(), ActiveBatchesAdapter.BatchClickListener {
    lateinit var binding: ActivityActiveBatchesBinding
    lateinit var viewModel: ActiveBatchesViewModel
    lateinit var adapter: ActiveBatchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_batches)
        viewModel = ViewModelProvider(this).get(ActiveBatchesViewModel::class.java)
        getAndSetIntentData()
        setCustomActionBarListener()
        setAdapter()
    }

    private fun getAndSetIntentData() {
        viewModel.batchList =
            intent.getParcelableArrayListExtra<NewMessageData>(batch_list) as ArrayList<NewMessageData>
        viewModel.courseName = intent.getStringExtra(course_name)!!
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, viewModel.courseName +" Batches")
    }

    private fun setActionBar() {
        supportActionBar?.title = viewModel.courseName + "Batches"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setAdapter() {
        adapter = ActiveBatchesAdapter(this)
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

    override fun onBatchClicked(batchData: NewMessageData) {

    }

    override fun onOverViewClicked(batchData: NewMessageData) {
        MyClassDetails.launchActivity(batchData.batchId.toString(),this,0)
    }

    override fun onStudyMaterialClicked(batchData: NewMessageData) {
        MyClassDetails.launchActivity(batchData.batchId.toString(),this,1)
    }

    override fun onExamClicked(batchData: NewMessageData) {
        ExamsActivity.launchActivity(this,batchData.courseId!!)
    }

    override fun onCompletionRequestClicked(batchData: NewMessageData) {
        MyClassDetails.launchActivity(batchData.batchId.toString(),this,3)
    }
}