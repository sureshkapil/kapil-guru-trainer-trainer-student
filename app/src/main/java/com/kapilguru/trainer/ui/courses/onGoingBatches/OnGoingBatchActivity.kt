package com.kapilguru.trainer.ui.courses.onGoingBatches

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityOnGoingBatchBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.onGoingBatches.viewModel.OnGoingBatchViewModel
import com.kapilguru.trainer.ui.courses.onGoingBatches.viewModel.OnGoingBatchViewModelFactory

class OnGoingBatchActivity : AppCompatActivity() {

    lateinit var onGoingBatchViewModel: OnGoingBatchViewModel
    lateinit var onGoingBatchBinding: ActivityOnGoingBatchBinding
    lateinit var dialog : CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onGoingBatchBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_going_batch)

        onGoingBatchViewModel = ViewModelProvider(
            this,
            OnGoingBatchViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        ).get(OnGoingBatchViewModel::class.java)
        dialog = CustomProgressDialog(this)
        onGoingBatchBinding.onGoingViewModel = onGoingBatchViewModel
        onGoingBatchBinding.lifecycleOwner = this

        onGoingBatchViewModel.fetchOnGoingBatchApi("8")

        observeData()
    }

    private fun observeData() {
        onGoingBatchViewModel.resultApiData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    /*  it.data?.let { it1 ->
                          it1.data?.let { it2 -> adapter.setCourseList(it2) }
                      }*/
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