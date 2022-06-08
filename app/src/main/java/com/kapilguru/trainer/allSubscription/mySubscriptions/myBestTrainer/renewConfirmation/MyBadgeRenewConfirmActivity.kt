package com.kapilguru.trainer.allSubscription.mySubscriptions.myBestTrainer.renewConfirmation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.allSubscription.mySubscriptions.myBestTrainer.renewConfirmation.viewModel.MyBadgeRenewConfirmViewModel
import com.kapilguru.trainer.allSubscription.mySubscriptions.myBestTrainer.renewConfirmation.viewModel.MyBadgeRenewConfirmViewModelFactory
import com.kapilguru.trainer.databinding.ActivityMyBadgeRenewConfirmBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.payment.PaymentActivity
import com.kapilguru.trainer.payment.PaymentStatusActivity
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest
import com.kapilguru.trainer.payment.model.TransactionStatusResponse

class MyBadgeRenewConfirmActivity : PaymentActivity(), PaymentActivity.PaymentStatusListener {
    private val TAG = "MyBadgeRenewConfirmActivity"
    lateinit var viewModel: MyBadgeRenewConfirmViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var binding: ActivityMyBadgeRenewConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_badge_renew_confirm)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, MyBadgeRenewConfirmViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(MyBadgeRenewConfirmViewModel::class.java)
        binding.viewModel = viewModel
        setCustomActionBar()
        getIntentData()
        binding.viewModel = viewModel
        setClickListeners()
        observeViewModelData()
    }

    override fun setPaymentListener() {
        paymentStatusListener = this
    }

    private fun setCustomActionBar() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.badge_subscription_renew_confirmation))
    }

    private fun getIntentData() {
        viewModel.myBestTrainerData = intent.getParcelableExtra(MY_BEST_TRAINER_DATA)!!

        viewModel.initiateTransactionRequest = addTransactionRequestData(viewModel.initiateTransactionRequest)
    }

    private fun setClickListeners() {
        binding.actvMakePayment.setOnClickListener {
            makePayment()
        }
    }

    private fun makePayment() {
        val initiateTransactionRequest = viewModel.initiateTransactionRequest
        initiateTransactionRequest.grandTotal = binding.actvGrandTotalValue.text.toString().toDouble()
        initiateTransactionRequest.centralGST = binding.actvCgstAmountValue.text.toString().toDouble()
        initiateTransactionRequest.stateGST = binding.actvSgstAmountValue.text.toString().toDouble()
        initiateTransactionRequest.amount = viewModel.myBestTrainerData.subscriptionAmount!!.toDouble()
        initiateTransactionRequest.productType = SUBSCRIPTION_BADGE
        initiateTransactionRequest.productId = viewModel.myBestTrainerData.subsId!!
        callInitiateTransactionApi(initiateTransactionRequest)
    }

    private fun observeViewModelData() {
        observePackageInitTransactionResponse()
        observePackageTransactionStatusApi()
    }

    private fun observePackageInitTransactionResponse() {
        paymentViewModel.initiateTransactionResponse.observe(this, Observer { initTransApiRes ->
            when (initTransApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    parseInitTransApiResponse(initTransApiRes.data?.initiateTransResData!!)
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observePackageTransactionStatusApi() {
        paymentViewModel.transactionStatusResponse.observe(this, Observer { transStatusApiRes ->
            when (transStatusApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    parseTransactionStatusResponse(transStatusApiRes.data)
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    companion object {
        val MY_BEST_TRAINER_DATA = "MY_BEST_TRAINER_DATA"

        fun launchActivity(activity: Activity, myBestTrainerData: MyBestTrainerData) {
            val intent = Intent(activity, MyBadgeRenewConfirmActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putParcelable(MY_BEST_TRAINER_DATA, myBestTrainerData)
                putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }

    override fun onPaymentActivityResult(status: PaymentStatus) {
        val courseId = viewModel.myBestTrainerData.courseId
        callBadgeTransactionStatusApi(courseId)
    }

    override fun onPaymentTransactionStatusResult(transactionStatusResponse: TransactionStatusResponse) {
        PaymentStatusActivity.launchActivity(transactionStatusResponse,this)
    }
}