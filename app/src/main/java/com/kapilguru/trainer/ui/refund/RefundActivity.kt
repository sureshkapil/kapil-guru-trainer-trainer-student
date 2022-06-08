package com.kapilguru.trainer.ui.refund

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityRefundBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.refund.adapter.RefundAdapter
import com.kapilguru.trainer.ui.refund.model.RefundData
import com.kapilguru.trainer.ui.refund.model.RefundViewModel
import com.kapilguru.trainer.ui.refund.model.RefundViewModelFactory
import kotlinx.android.synthetic.main.activity_refund.*

class RefundActivity : BaseActivity() , RefundAdapter.onItemClickRefund {

    lateinit var refundBottomSheetFragment: RefundBottomSheetFragment
    lateinit var refundBinding: ActivityRefundBinding
    lateinit var refundViewModel: RefundViewModel
    lateinit var refundAdapter: RefundAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refundBinding = DataBindingUtil.setContentView(this, R.layout.activity_refund)

        refundViewModel = ViewModelProvider(
            this, RefundViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),application)
        ).get(
            RefundViewModel::class.java
        )
        dialog = CustomProgressDialog(this)
        refundBinding.viewModel = refundViewModel
        refundBinding.lifecycleOwner = this
        refundAdapter= RefundAdapter(this as RefundAdapter.onItemClickRefund)

        refundBinding.refundRecyclerView.adapter=refundAdapter
        this.setActionbarBackListener(this, refundBinding.actionbar, getString(R.string.refund_request_list))
        observeViewModelData()
    }

    private fun observeViewModelData() {
        refundViewModel.refundListApi.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.let { refundListApi ->
                        refundListApi.refundData.let { refundData ->
                            refundAdapter.setRefundList(refundData)
                        }
                    }
                    dialog.dismissLoadingDialog()
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

    override fun onItemClick(refundData: RefundData) {
        refundBottomSheetFragment= RefundBottomSheetFragment.newInstance(refundData)
        refundBottomSheetFragment.show(supportFragmentManager,"")

    }
}