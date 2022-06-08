package com.kapilguru.trainer.ui.courses.batchesList.batchStudents

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityBatchStudentsListBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.batchesList.batchStudents.adapter.BatchStudentsAdapter
import com.kapilguru.trainer.ui.courses.batchesList.batchStudents.viewModel.BatchStudentListModelFactory
import com.kapilguru.trainer.ui.courses.batchesList.batchStudents.viewModel.BatchStudentListViewModel


class BatchStudentsListActivity : AppCompatActivity() {
    lateinit var batchStudentListViewModel: BatchStudentListViewModel
    lateinit var batchStudentsListBinding : ActivityBatchStudentsListBinding
    lateinit var batchStudentsAdapter: BatchStudentsAdapter
    lateinit var dialog : CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title ="Batch Student List"
        batchStudentsListBinding = DataBindingUtil.setContentView(this,R.layout.activity_batch_students_list)

        batchStudentListViewModel = ViewModelProvider(
            this, BatchStudentListModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        ).get(
            BatchStudentListViewModel::class.java
        )
        dialog = CustomProgressDialog(this)

        batchStudentsListBinding.batchStudentViewModel = batchStudentListViewModel
        batchStudentsListBinding.lifecycleOwner = this

        batchStudentsAdapter = BatchStudentsAdapter(this)
        batchStudentsListBinding.recyclerViewStudent.adapter = batchStudentsAdapter

        observeViewModelData()

    }

    private fun observeViewModelData() {
        batchStudentListViewModel.batchStudentListApi.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.let { it1 ->
                        it1.data.let { it2 -> batchStudentsAdapter.setBatchStudentsList(it2) }
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
}