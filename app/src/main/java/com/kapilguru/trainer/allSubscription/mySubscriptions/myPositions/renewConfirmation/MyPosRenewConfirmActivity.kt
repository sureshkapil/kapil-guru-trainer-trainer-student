package com.kapilguru.trainer.allSubscription.mySubscriptions.myPositions.renewConfirmation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.allSubscription.mySubscriptions.myPositions.renewConfirmation.viewModel.MyPosRenewConfirmViewModel
import com.kapilguru.trainer.allSubscription.mySubscriptions.myPositions.renewConfirmation.viewModel.MyPosRenewConfirmViewModelFactory
import com.kapilguru.trainer.databinding.ActivityMyPosSubRenewConfirmBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.payment.PaymentActivity
import com.kapilguru.trainer.payment.PaymentStatusActivity
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest
import com.kapilguru.trainer.payment.model.TransactionStatusResponse
import com.kapilguru.trainer.preferences.StorePreferences

class MyPosRenewConfirmActivity : PaymentActivity(), PaymentActivity.PaymentStatusListener {
    lateinit var binding : ActivityMyPosSubRenewConfirmBinding
    lateinit var viewModel : MyPosRenewConfirmViewModel
    lateinit var progressDialog :CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_pos_sub_renew_confirm)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        setCustomActionBarListener()
        viewModel = ViewModelProvider(this, MyPosRenewConfirmViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(MyPosRenewConfirmViewModel::class.java)
        getIntentData()
        binding.viewModel = viewModel
        setClickListeners()
        setData()
        observeViewModelData()
    }

    override fun setPaymentListener() {
        paymentStatusListener = this
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.position_subscription_confirmation))
    }

    private fun getIntentData() {
        viewModel.positionSubscriptionData = intent.getParcelableExtra(MY_POSITION_DATA)!!

        viewModel.initiateTransactionRequest = addTransactionRequestData(viewModel.initiateTransactionRequest)
    }

    private fun setClickListeners(){
        binding.actvMakePayment.setOnClickListener {
            makePayment()
        }
    }

    private fun makePayment(){
        val initiateTransactionRequest = viewModel.initiateTransactionRequest
        initiateTransactionRequest.grandTotal = binding.actvGrandTotalValue.text.toString().toDouble()
        initiateTransactionRequest.centralGST = binding.actvCgstAmountValue.text.toString().toDouble()
        initiateTransactionRequest.stateGST = binding.actvSgstAmountValue.text.toString().toDouble()
        initiateTransactionRequest.amount = viewModel.positionSubscriptionData.subscriptionAmount!!.toDouble()
        initiateTransactionRequest.productType = SUBSCRIPTION_POSITION
        initiateTransactionRequest.productId = viewModel.positionSubscriptionData.subsId!!
        callInitiateTransactionApi(initiateTransactionRequest)
    }

    private fun setData(){
        val trainerName = StorePreferences(this).userName
        binding.tvTrainerNameValue.text = trainerName
        binding.tvTrainerIdValue.text = viewModel.positionSubscriptionData.userId.toString()
        binding.actvSubscUntillValue.text = getExpiryDate(viewModel.positionSubscriptionData.expiryDate)
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

    companion object{
        val MY_POSITION_DATA = "MY_POSITION_DATA"

        fun launchActivity(activity : Activity,myPositionData: MyPositionData){
            val intent = Intent(activity,MyPosRenewConfirmActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(MY_POSITION_DATA, myPositionData)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    override fun onPaymentActivityResult(status: PaymentStatus) {
        val positionSubscriptionData = viewModel.positionSubscriptionData
        if (status == PaymentStatus.SUCCESS) {
            val courseId = positionSubscriptionData.courseId!!
            val coursePositionNumber = positionSubscriptionData.coursePositionNum!!
            callPositionTransactionStatusApi(courseId, coursePositionNumber)
        }
    }

    override fun onPaymentTransactionStatusResult(transactionStatusResponse: TransactionStatusResponse) {
        PaymentStatusActivity.launchActivity(transactionStatusResponse,this)
    }
}