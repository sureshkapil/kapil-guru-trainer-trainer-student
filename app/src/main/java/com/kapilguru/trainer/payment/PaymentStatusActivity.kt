package com.kapilguru.trainer.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityPaymentStatusBinding
import com.kapilguru.trainer.payment.model.TransactionStatusResponse
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.home.HomeActivity
import com.kapilguru.trainer.ui.profile.ProfileUtils

class PaymentStatusActivity : BaseActivity() {
    lateinit var binding: ActivityPaymentStatusBinding
    lateinit var mTransactionStatusResponse: TransactionStatusResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLateInitVariables()
        getIntentData()
        setCustomActionBarListener()
        setClickListeners()
    }

    private fun initializeLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_status)
        binding.lifecycleOwner = this
    }

    private fun getIntentData() {
        mTransactionStatusResponse = intent.getParcelableExtra(TRANSACTION_STATUS_RESPONSE_KEY)!!
        binding.model = mTransactionStatusResponse
    }

    private fun setCustomActionBarListener() {
        val activityName = getString(R.string.payment_status_activity)
        setActionbarBackListener(this, binding.actionbar, activityName, false, null)
    }

    private fun setClickListeners() {
        binding.btnDone.setOnClickListener {
            if(StorePreferences(this).isProfileUpdated == 1){
                finish()
            }else{
                startActivity(Intent(this,HomeActivity::class.java))
                finishAffinity()
            }
        }
    }

    companion object {
        val TRANSACTION_STATUS_RESPONSE_KEY = "TRANSACTION_STATUS_RESPONSE"

        fun launchActivity(transactionStatusResponse: TransactionStatusResponse, activity: Activity) {
            val intent = Intent(activity, PaymentStatusActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelable(TRANSACTION_STATUS_RESPONSE_KEY, transactionStatusResponse)
                }
                putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }
}
