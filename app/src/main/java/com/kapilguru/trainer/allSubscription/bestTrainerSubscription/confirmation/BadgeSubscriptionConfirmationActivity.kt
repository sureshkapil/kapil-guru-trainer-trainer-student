package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.confirmation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.SUBSCRIPTION_BADGE
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.confirmation.viewModel.BadgeSubConfirmViewModel
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.confirmation.viewModel.BadgeSubConfirmViewModelFactory
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.BestTrainerCourseData
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.databinding.ActivityBadgeSubscriptionConfirmationBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.payment.PaymentActivity
import com.kapilguru.trainer.payment.PaymentStatusActivity
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest
import com.kapilguru.trainer.payment.model.TransactionStatusResponse

class BadgeSubscriptionConfirmationActivity : PaymentActivity(), PaymentActivity.PaymentStatusListener {
    private val TAG = "BadgeSubscriptionConfirmationActivity"
    lateinit var binding: ActivityBadgeSubscriptionConfirmationBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var viewModel: BadgeSubConfirmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_badge_subscription_confirmation)
        binding.lifecycleOwner = this
        setCustomActionBarListener()
        viewModel = ViewModelProvider(this, BadgeSubConfirmViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(BadgeSubConfirmViewModel::class.java)
        progressDialog = CustomProgressDialog(this)
        getIntentData()
        binding.viewModel = viewModel
        setClickListeners()
        observeViewModelData()
        setData()
    }

    override fun setPaymentListener() {
        paymentStatusListener = this
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.badge_subscription_confirmation))
    }

    private fun getIntentData() {
        viewModel.badgeData = intent.getParcelableExtra(BADGE_DATA)!!
        viewModel.courseData = intent.getParcelableExtra(COURSE_DATA)!!

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
        initiateTransactionRequest.amount = viewModel.badgeData.subscriptionFee
        initiateTransactionRequest.productType = SUBSCRIPTION_BADGE
        initiateTransactionRequest.productId = viewModel.courseData.id
        callInitiateTransactionApi(initiateTransactionRequest)
    }

    private fun setData() {
        val toShow = getExpiryDate(null)
        binding.actvSubscUntillValue.text = toShow
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
        val BADGE_DATA = "BADGE_DATA_KEY"
        val COURSE_DATA = "COURSE_DATA_KEY"

        fun launchActivity(activity: Activity, badgeData: AllSubscriptionsData, courseData: BestTrainerCourseData) {
            val intent = Intent(activity, BadgeSubscriptionConfirmationActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelable(BADGE_DATA, badgeData)
                    putParcelable(COURSE_DATA, courseData)
                }
                putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }

    override fun onPaymentActivityResult(status: PaymentStatus) {
        val courseId = viewModel.courseData.id
        callBadgeTransactionStatusApi(courseId)
    }

    override fun onPaymentTransactionStatusResult(transactionStatusResponse: TransactionStatusResponse) {
        PaymentStatusActivity.launchActivity(transactionStatusResponse, this)
    }
}