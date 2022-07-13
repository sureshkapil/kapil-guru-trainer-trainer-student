package com.kapilguru.trainer.ui.courses.batchesList

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityBatchListDetailsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.studentsList.view.StudentList
import com.kapilguru.trainer.ui.courses.adapter.BatchListAdapter
import com.kapilguru.trainer.ui.courses.add_batch.AddBatchActivity
import com.kapilguru.trainer.ui.courses.add_batch.NewAddBatchActivity
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListResponse
import com.kapilguru.trainer.ui.courses.batchesList.viewModel.BatchListViewModel
import com.kapilguru.trainer.ui.courses.batchesList.viewModel.BatchListViewModelFactory
import kotlinx.android.synthetic.main.activity_batch_list_details.*

class BatchListDetailsActivity : BaseActivity(), BatchListAdapter.OnItemClickedForBatch, TwoButtonDialogInteractor {
    lateinit var viewModel: BatchListViewModel
    lateinit var binding: ActivityBatchListDetailsBinding
    lateinit var batchAdapter: BatchListAdapter
    lateinit var dialog: CustomProgressDialog
    var courseId: Int = 0
    var isSubmitted: Int = 0 // to show is availabale in web or only in APP
    var courseName: String? = ""
    var batchId: String?=""
    var deletedIndex: Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_batch_list_details)
        dialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, BatchListViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application, ))
            .get(BatchListViewModel::class.java)
        fetchDataFromPreviousActivity()
        setCustomActionBarListener()
        binding.batchListViewModel = viewModel
        binding.lifecycleOwner = this
        setAdapter()
        observeViewModelData()
        setClickListeners()
    }

    private fun fetchDataFromPreviousActivity() {
        courseId = intent.getIntExtra("courseId", 0)
        isSubmitted = intent.getIntExtra(IS_SUBMITTED_PARAM, 0)
        viewModel.courseid.value = courseId.toString()
        courseName = intent.getStringExtra(COURSE_NAME_PARAM).toString() + " Batches"
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, courseName!!)
    }

    private fun setAdapter() {
        batchAdapter = BatchListAdapter(this as BatchListAdapter.OnItemClickedForBatch)
        binding.batchRecyclerView.adapter = batchAdapter
    }

    private fun setClickListeners() {
        buttonAddCourse.setOnClickListener {
            val intent = Intent(this, AddBatchActivity::class.java)
            intent.putExtra("courseId", courseId)
            intent.putExtra(IS_SUBMITTED_PARAM, isSubmitted)
            startActivity(intent)
        }

//        buttonAddCourse.setOnClickListener {
//            val intent = Intent(this, NewAddBatchActivity::class.java)
//            intent.putExtra("courseId", courseId)
//            startActivity(intent)
//        }
    }

    private fun observeViewModelData() {
        // get batch List API response
        viewModel.constructBatchListRequest(viewModel.courseid.value!!)
        viewModel.batchListApi.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.let { batchListApi ->
                        batchListApi.batchListResponse.let { batchListResponse ->
                            batchAdapter.setBatchList(batchListResponse)
                            viewModel.totalBatches.value = batchListResponse.size.toString()
                        }
                    }
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    if (it.data?.status != 200) {
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }
        })


        // on delete batch item   API response
        viewModel.deleteBatchResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.let { batchListApi ->
                        batchAdapter.updateAdapterPosition(deletedIndex)
                        resetSelectedPositionData()
                    }
                    dialog.dismissLoadingDialog()
                    showRemovedToast()
                }

                Status.ERROR -> {
                    resetSelectedPositionData()
                    dialog.dismissLoadingDialog()
                    if (it.data?.status != 200) {
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }
        })
    }

    private fun showRemovedToast() {
        showAppToast(this,"Batch is removed successfully")
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
            builder.create()
        }
        alertDialog.show()
    }

    override fun onEditItemClick(batchDetails: BatchListResponse) {
        if (batchDetails.studentsCount > 0) {
            Toast.makeText(this, getString(R.string.enrolled_students_edit_batch_txt), Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, AddBatchActivity::class.java)
            intent.putExtra(EDIT_BATCH_ID_PARAM, batchDetails.batchId)
            intent.putExtra(IS_FROM_EDIT_PARAM, true)
            intent.putExtra(IS_SUBMITTED_PARAM, isSubmitted)
            intent.putExtra("courseId", batchDetails.courseId)
            startActivity(intent)
        }
    }

    override fun onStudentsClicked(batchListResponse: BatchListResponse) {
        val intent = Intent(this, StudentList::class.java)
        intent.putExtra(PARAM_IS_FROM, PARAM_IS_FROM_BATCH)
        intent.putExtra(PARAM_BATCH_ID,batchListResponse.batchId.toString())
        startActivity(intent)
    }

    override fun onDeleteClick(batchCode: String, batchId: String, studentCount: Int, index: Int) {
        if(studentCount>0) {
            showSingleButtonErrorDialog(this,message = "There are already students enrolled for  $batchCode")
        } else {
            this.batchId = batchId
            this.deletedIndex = index
            showTwoButtonDialog(batchCode)
        }
    }

    override fun onShareClick(shareText: String) {
        shareIntent(BuildConfig.BASE_URL+ COURSE_DETAILS+shareText,this)
    }

    private fun showTwoButtonDialog(courseTitle: String) {
        val fm: FragmentManager = supportFragmentManager
        val editNameDialogFragment: TwoButtonDialog = TwoButtonDialog.newInstance(
            String.format(getString(R.string.you_want_to_delete),courseTitle),this)
        editNameDialogFragment.show(fm, "two_button_dialog")
    }

    override fun onNegativeClick() {
        resetSelectedPositionData()
    }

    private fun resetSelectedPositionData() {
        batchId = ""
        deletedIndex = -1
    }

    override fun onPositiveClick() {
        this.batchId?.let {
            viewModel.deleteBatch(it)
        }

    }
}