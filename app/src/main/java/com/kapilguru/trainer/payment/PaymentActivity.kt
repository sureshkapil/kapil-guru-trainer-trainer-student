package com.kapilguru.trainer.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest
import com.kapilguru.trainer.payment.model.InitiateTransactionResData
import com.kapilguru.trainer.payment.model.TransactionStatusResponse
import com.kapilguru.trainer.payment.viewModel.PaymentViewModel
import com.kapilguru.trainer.payment.viewModel.PaymentViewModelFactory
import com.kapilguru.trainer.preferences.StorePreferences
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import com.paytm.pgsdk.TransactionManager
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

abstract class PaymentActivity : BaseActivity() {
    private val PAYMENT_TAG = "PaymentActivity"
    lateinit var paymentViewModel: PaymentViewModel
    var paymentStatusListener: PaymentStatusListener? = null
    val paytmRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentViewModel = ViewModelProvider(this, PaymentViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(PaymentViewModel::class.java)
    }

    abstract fun setPaymentListener()

    /*This method adds the common data which is required for transaction*/
    fun addTransactionRequestData(initiateTransactionRequest: InitiateTransactionRequest): InitiateTransactionRequest {
        initiateTransactionRequest.userCode = StorePreferences(this).userCode
        initiateTransactionRequest.userId = StorePreferences(this).userId
        val userId = initiateTransactionRequest.userId
        val orderId = "O_000" + userId + "_" + System.currentTimeMillis().toString()
        initiateTransactionRequest.orderId = orderId
        return initiateTransactionRequest
    }

    /*@param - oldExpiryDate is the expiry date of existing subscription.
    * can be empty if user is not having any subscription.*/
    fun getExpiryDate(oldExpiryDate: String?): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        if (oldExpiryDate == null) {
            calendar.time = format.parse(format.format(calendar.time))
        } else {
            calendar.time = format.parse(oldExpiryDate)
        }
        calendar.add(Calendar.DAY_OF_YEAR, 365)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy")
        val toReturn = outputFormat.format(calendar.time)
        Log.d(PAYMENT_TAG, "getExpiryDate : toReturn : " + toReturn)
        return toReturn
    }

    /*Initiate transaction Api Request is same for all subscriptions*/
    fun callInitiateTransactionApi(initiateTransactionRequest: InitiateTransactionRequest) {
        paymentViewModel.callInitiateTransactionApi(initiateTransactionRequest)
    }

    /*Separate methods are written for transaction status api as the request body differs from each other*/
    fun callPackageTransactionStatusApi() {
        val transactionStatusRequest = paymentViewModel.mInitiateTransactionRequest
        paymentViewModel.callTransactionStatusApi(transactionStatusRequest)
    }

    fun callPositionTransactionStatusApi(courseId: Int, coursePositionNumber: Int) {
        val transactionStatusRequest = paymentViewModel.mInitiateTransactionRequest
        transactionStatusRequest.courseId = courseId
        transactionStatusRequest.coursePositionNum = coursePositionNumber
        paymentViewModel.callTransactionStatusApi(transactionStatusRequest)
    }

    fun callBadgeTransactionStatusApi(courseId: Int) {
        val transactionStatusRequest = paymentViewModel.mInitiateTransactionRequest
        transactionStatusRequest.userCode = null
        transactionStatusRequest.courseId = courseId
        paymentViewModel.callTransactionStatusApi(transactionStatusRequest)
    }

    fun parseInitTransApiResponse(initTransResString: String) {
        val initTransResData = InitiateTransactionResData().getObject(initTransResString)
        if (initTransResData.body?.resultInfo?.resultStatus == "S") {
            openPaytm(initTransResData.body!!.txnToken)
        }
    }

    fun parseTransactionStatusResponse(transactionStatusRes: TransactionStatusResponse?) {
        transactionStatusRes?.let {
            if (it.body?.resultInfo?.resultStatus == TRANSACTION_SUCCESS) {
                updateIsSubscribed(true)
                informTransactionStatusResponse(it)
            }
        } ?: run {
        }
    }

    private fun openPaytm(txnToken: String) {
        val isLive = false
        val host = if (isLive) "https://securegw.paytm.in/" else "https://securegw-stage.paytm.in/"

        val orderId = paymentViewModel.mInitiateTransactionRequest.orderId
        val mid = "KapilG71666698302198"
        val amount = paymentViewModel.mInitiateTransactionRequest.grandTotal.toString()
        val callbackUrl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId
        val paytmOrder = PaytmOrder(orderId, mid, txnToken, amount, callbackUrl)

        val transactionManager = TransactionManager(paytmOrder, object : PaytmPaymentTransactionCallback {
            override fun onTransactionResponse(p0: Bundle?) {

            }

            override fun networkNotAvailable() {
                showAppToast(applicationContext, "No Network available")
            }

            override fun onErrorProceed(p0: String?) {
                showAppToast(applicationContext, "onErrorProceed : $p0")
            }

            override fun clientAuthenticationFailed(p0: String?) {
                showAppToast(applicationContext, "clientAuthenticationFailed")
            }

            override fun someUIErrorOccurred(p0: String?) {
                showAppToast(applicationContext, "UI Error Occurred")
            }

            override fun onErrorLoadingWebPage(p0: Int, p1: String?, p2: String?) {
                showAppToast(applicationContext, "Error loading Web Page")
            }

            override fun onBackPressedCancelTransaction() {
                showAppToast(applicationContext, "Transaction Cancelled due to back press")
            }

            override fun onTransactionCancel(p0: String?, p1: Bundle?) {
                showAppToast(applicationContext, "Transaction ")
            }

        })
        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(this, paytmRequestCode)
    }

    private fun updateIsSubscribed(isSubscribed: Boolean) {
        if (isSubscribed) {
            StorePreferences(this).isSubscribed = 1
        } else {
            StorePreferences(this).isSubscribed = 0
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == paytmRequestCode) {
            val bundle = data?.extras
            bundle?.let {
                if (resultCode == RESULT_OK) {
                    val jsonObject = JSONObject(it.getString("response")!!)
                    if (jsonObject.optString(getString(R.string.payment_status)) == getString(R.string.payment_txn_success)) {
                        val paymentStatus = jsonObject.getString(getString(R.string.payment_status))
                        if (paymentStatus == getString(R.string.payment_txn_success)) {
                            informOnActivityResult(PaymentStatus.SUCCESS)
                        } else {
                            informOnActivityResult(PaymentStatus.FAILURE)
                        }
                    }
                } else {
                    informOnActivityResult(PaymentStatus.FAILURE)
                }
            }?: kotlin.run {
                informOnActivityResult(PaymentStatus.FAILURE)
            }
        }
    }

    private fun informOnActivityResult(status: PaymentStatus) {
        paymentStatusListener?.onPaymentActivityResult(status)
    }

    private fun informTransactionStatusResponse(transactionStatusResData: TransactionStatusResponse) {
        paymentStatusListener?.onPaymentTransactionStatusResult(transactionStatusResData)
    }

    interface PaymentStatusListener {
        fun onPaymentActivityResult(status: PaymentStatus)
        fun onPaymentTransactionStatusResult(transactionStatusResponse: TransactionStatusResponse)
    }

    enum class PaymentStatus {
        SUCCESS, FAILURE
    }
}