package com.kapilguru.trainer.allSubscription.positionSubscription.confirmation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.Companion.setPositionSubscriptionBackGround
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.positionSubscription.confirmation.viewModel.PositionSubConfirmViewModel
import com.kapilguru.trainer.allSubscription.positionSubscription.confirmation.viewModel.PositionSubConfirmViewModelFactory
import com.kapilguru.trainer.allSubscription.positionSubscription.model.TrainerCourseData
import com.kapilguru.trainer.databinding.ActivityPositionSubscriptionConfirmBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.payment.PaymentActivity
import com.kapilguru.trainer.payment.PaymentStatusActivity
import com.kapilguru.trainer.payment.model.TransactionStatusResponse

class PositionSubscriptionConfirmActivity : PaymentActivity(), PaymentActivity.PaymentStatusListener {
    private val TAG = "PositionSubscriptionConfirmActivity"
    lateinit var binding: ActivityPositionSubscriptionConfirmBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var viewModel: PositionSubConfirmViewModel
    private var positionNotSelected = -1
    private var mLastSelectedPosition = positionNotSelected

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_position_subscription_confirm)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        setCustomActionBarListener()
        viewModel = ViewModelProvider(this, PositionSubConfirmViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(PositionSubConfirmViewModel::class.java)
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
        viewModel.allSubscriptionsData = intent.getParcelableExtra(ALL_SUBSCRIPTION_DATA)!!
        val trainerCourseData = intent.getParcelableExtra<TrainerCourseData>(POS_SUB_DATA)!!
        viewModel.positionSubData = trainerCourseData
        viewModel.isLaunchedForRenewal = trainerCourseData.isOwned

        viewModel.initiateTransactionRequest = addTransactionRequestData(viewModel.initiateTransactionRequest)
    }

    private fun setData() {
        var expiryDate: String? = null
        if (viewModel.isLaunchedForRenewal) {
            expiryDate = viewModel.positionSubData.myPosSubs?.expiryDate!!
        }
        val toShow = getExpiryDate(expiryDate)
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

    private fun setClickListeners() {
        viewModel.positionSubData.isPosition_1_Occupied = true
        binding.tvPos1.setOnClickListener {
            val trainerCourseData = viewModel.positionSubData
            setBackground(it as TextView, trainerCourseData.isPosition_1_Occupied, "1", "1")
            setLastSelectedPosition(1, trainerCourseData.isPosition_1_Occupied)
        }
        binding.tvPos2.setOnClickListener {
            val trainerCourseData = viewModel.positionSubData
            setBackground(it as TextView, trainerCourseData.isPosition_2_Occupied, "2", "2")
            setLastSelectedPosition(2, trainerCourseData.isPosition_2_Occupied)
        }
        binding.tvPos3.setOnClickListener {
            val trainerCourseData = viewModel.positionSubData
            setBackground(it as TextView, trainerCourseData.isPosition_3_Occupied, "3", "3")
            setLastSelectedPosition(3, trainerCourseData.isPosition_3_Occupied)
        }
        binding.tvPos4.setOnClickListener {
            val trainerCourseData = viewModel.positionSubData
            setBackground(it as TextView, trainerCourseData.isPosition_4_Occupied, "4", "4")
            setLastSelectedPosition(4, trainerCourseData.isPosition_4_Occupied)
        }
        binding.tvPos5.setOnClickListener {
            val trainerCourseData = viewModel.positionSubData
            setBackground(it as TextView, trainerCourseData.isPosition_5_Occupied, "5", "5")
            setLastSelectedPosition(5, trainerCourseData.isPosition_5_Occupied)
        }
        binding.actvMakePayment.setOnClickListener {
            checkAndMakePayment()
        }
    }

    private fun setBackground(
        textView: TextView, isPositionOccupied: Boolean, positionOfTextView: String,
        selectedPositionToSubscribe: String
    ) {
        if (mLastSelectedPosition != positionNotSelected) {
            setBackgroundOfPreviouslySelectedPosition(selectedPositionToSubscribe)
        }
        textView.setPositionSubscriptionBackGround(isPositionOccupied, positionOfTextView, selectedPositionToSubscribe)
    }

    private fun setBackgroundOfPreviouslySelectedPosition(selectedPositionToSubscribe: String) {
        val textView: TextView
        var isPositionOccupied = false
        var positionOfTextView: String = ""
        when (mLastSelectedPosition) {
            1 -> {
                textView = binding.tvPos1
                isPositionOccupied = viewModel.positionSubData.isPosition_1_Occupied
                positionOfTextView = "1"
            }
            2 -> {
                textView = binding.tvPos2
                isPositionOccupied = viewModel.positionSubData.isPosition_2_Occupied
                positionOfTextView = "2"
            }
            3 -> {
                textView = binding.tvPos3
                isPositionOccupied = viewModel.positionSubData.isPosition_3_Occupied
                positionOfTextView = "3"
            }
            4 -> {
                textView = binding.tvPos4
                isPositionOccupied = viewModel.positionSubData.isPosition_4_Occupied
                positionOfTextView = "4"
            }
            else -> {
                textView = binding.tvPos5
                isPositionOccupied = viewModel.positionSubData.isPosition_5_Occupied
                positionOfTextView = "5"
            }
        }
        textView.setPositionSubscriptionBackGround(isPositionOccupied, positionOfTextView, selectedPositionToSubscribe)
    }

    /*this method sets the value of last selected position as
    * -1 when the selected position is occupied, and as
    * the selected position if it is not occupied*/
    private fun setLastSelectedPosition(selectedPosition: Int, isSelectedPositionOccupied: Boolean) {
        mLastSelectedPosition = if (isSelectedPositionOccupied) {
            positionNotSelected
        } else {
            selectedPosition
        }
    }

    private fun checkAndMakePayment() {
        if (!viewModel.isLaunchedForRenewal && mLastSelectedPosition == -1) {
            showAppToast(this, "Please select a position to Subscribe")
        } else {
            val initiateTransactionRequest = viewModel.initiateTransactionRequest
            initiateTransactionRequest.amount = viewModel.allSubscriptionsData?.subscriptionFee
            initiateTransactionRequest.grandTotal = binding.actvGrandTotalValue.text.toString().toDouble()
            initiateTransactionRequest.centralGST = binding.actvCgstAmountValue.text.toString().toDouble()
            initiateTransactionRequest.stateGST = binding.actvSgstAmountValue.text.toString().toDouble()
            initiateTransactionRequest.productType = SUBSCRIPTION_POSITION
            initiateTransactionRequest.productId = viewModel.positionSubData.id
            callInitiateTransactionApi(initiateTransactionRequest)
        }
    }

    companion object {
        val ALL_SUBSCRIPTION_DATA = "AllSubscriptionsData"
        val POS_SUB_DATA = "POS_SUB_DATA"

        fun launchActivity(activity: Activity, trainerCourseData: TrainerCourseData, allSubscriptionsData: AllSubscriptionsData) {
            val intent = Intent(activity, PositionSubscriptionConfirmActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelable(POS_SUB_DATA, trainerCourseData)
                    putParcelable(ALL_SUBSCRIPTION_DATA, allSubscriptionsData)
                }
                putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }

    override fun onPaymentActivityResult(status: PaymentStatus) {
        val trainerCourseData = viewModel.positionSubData
        if (status == PaymentStatus.SUCCESS) {
            val courseId = trainerCourseData.id
            val coursePositionNumber = trainerCourseData.positionToSubscribe
            callPositionTransactionStatusApi(courseId, coursePositionNumber)
        }
    }

    override fun onPaymentTransactionStatusResult(transactionStatusResponse: TransactionStatusResponse) {
        PaymentStatusActivity.launchActivity(transactionStatusResponse,this)
    }
}