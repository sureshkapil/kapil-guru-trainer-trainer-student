package com.kapilguru.trainer.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityPaymentPreviewBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest
import com.kapilguru.trainer.payment.model.TransactionStatusResponse
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.showAppToast

class PaymentPreviewActivity : PaymentActivity(),PaymentActivity.PaymentStatusListener  {
    lateinit var binding : ActivityPaymentPreviewBinding
    lateinit var progressDialog : CustomProgressDialog
    lateinit var mInitiateTransactionRequest: InitiateTransactionRequest
    var mDurationDays = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setCustomActionBarListener()
        getIntentData()
        showData()
        observeViewModelData()
        setClickListeners()
    }

    override fun setPaymentListener() {
      paymentStatusListener = this
    }

    private fun initLateInitVariables(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_preview)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
    }

    private fun setCustomActionBarListener() {
        val activityName = getString(R.string.payment_preview_activity_name)
        setActionbarBackListener(this, binding.actionbar, activityName)
    }

    private fun getIntentData(){
        mInitiateTransactionRequest = intent.getParcelableExtra(INIT_TRANS_REQ)!!
        mDurationDays = intent.getIntExtra(DURATION_DAYS,0)
        addTransactionRequestData(mInitiateTransactionRequest)
        binding.model = mInitiateTransactionRequest
        binding.duration = mDurationDays
    }

    private fun showData(){
        binding.actvNameValue.text = StorePreferences(this).userName
    }

    private fun observeViewModelData(){
        observePackageInitTransactionResponse()
        observePackageTransactionStatusApi()
    }

    private fun observePackageInitTransactionResponse(){
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

    private fun setClickListeners() {
        binding.btnPay.setOnClickListener {
            mInitiateTransactionRequest.grandTotal = binding.actvGrandTotalValue.text.toString().toDouble()
            mInitiateTransactionRequest.centralGST = binding.actvCgstAmountValue.text.toString().toDouble()
            mInitiateTransactionRequest.stateGST = binding.actvSgstAmountValue.text.toString().toDouble()
            callInitiateTransactionApi(mInitiateTransactionRequest)
        }
    }

    override fun onPaymentActivityResult(status: PaymentStatus) {
        if(status == PaymentStatus.SUCCESS){
            callPackageTransactionStatusApi()
        }else{
            showAppToast(this,"Transaction Cancelled")
        }
    }

    override fun onPaymentTransactionStatusResult(transactionStatusResponse : TransactionStatusResponse) {
        PaymentStatusActivity.launchActivity(transactionStatusResponse,this)
    }

    companion object{
        const val INIT_TRANS_REQ = "INIT_TRANS_REQ"
        const val DURATION_DAYS = "DURATION_DAYS"

        /*Mandatory values in - initiateTransactionRequest param should have amount, product ID, product type*/
        fun launchActivity(initiateTransactionRequest: InitiateTransactionRequest,durationDays : Int,activity : Activity){
            val intent = Intent(activity,PaymentPreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(INIT_TRANS_REQ, initiateTransactionRequest)
            bundle.putInt(DURATION_DAYS,durationDays)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
}