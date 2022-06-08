package com.kapilguru.trainer.ui.earnings.webinarAmount

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityAmountViewDetailsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.earnings.referralDetails.ReferralAmount
import com.kapilguru.trainer.ui.earnings.referralDetails.ReferralAmountRecycler
import com.kapilguru.trainer.ui.earnings.referralDetails.ReferralAmountViewModel
import com.kapilguru.trainer.ui.earnings.referralDetails.ReferralAmountViewModelFactory
import kotlinx.android.synthetic.main.activity_amount_view_details.*
import org.json.JSONArray
import org.json.JSONObject

class WebinarAmount : BaseActivity() {

    lateinit var binding: ActivityAmountViewDetailsBinding
    lateinit var viewModel: WebinarAmountViewModel
    lateinit var amountAmountRecycler: WebinarAmountRecycler
    private val TAG = "AmountViewDetails"
    var shouldReadAvailable: Boolean = true
    lateinit var dialog: CustomProgressDialog
    var isFromHistory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_amount_view_details)
        viewModel = ViewModelProvider(
            this, WebinarAmountViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)
        ).get(WebinarAmountViewModel::class.java)
        binding.amountType.text = resources.getString(R.string.referral_amount)
        setCustomActionBarListener()
        readIntent()
        dialog = CustomProgressDialog(this)
        setRecycler()
        viewModelObserver()

    }

    private fun readIntent() {
        shouldReadAvailable = intent.getBooleanExtra(PARAM_IS_AVAILABLE, false)
        isFromHistory = intent.getStringExtra(PARAM_IS_FROM)
    }

    private fun setRecycler() {
        amountAmountRecycler = WebinarAmountRecycler()
        recycler.adapter = amountAmountRecycler
    }

    private fun viewModelObserver() {
        isFromHistory?.let {
            val data  = intent.getParcelableArrayListExtra<WebinarAmountResponse>(PARAM_AMOUNT_LIST_INFO)
            data?.let {
                amountAmountRecycler.listOfItems = data
            }
        }?: kotlin.run {
            viewModel.earningsDetailsListApi.observe(this, Observer { response ->

                when (response.status) {
                    Status.LOADING -> {
                        dialog.showLoadingDialog()
                    }

                    Status.SUCCESS -> {
                        response.data?.let {
                            var coursesStr: String? = null
                            val json: JSONObject = when (shouldReadAvailable) {
                                true -> {
                                    JSONObject(it.apiAllData.available)
                                }
                                else -> {
                                    JSONObject(it.apiAllData.expected)
                                }
                            }
                            coursesStr = json.getString("webinars")
                            if (coursesStr != "null") {
                                val coursesArray = JSONArray(coursesStr)
                                val referralAmountDetailsList = arrayListOf<WebinarAmountResponse>()
                                for (i in 0 until coursesArray.length()) {
                                    val jsonstr = coursesArray.getJSONObject(i)
                                    referralAmountDetailsList.add(WebinarAmountResponse().getWebinarAmountDetails(jsonstr))
                                }
                                amountAmountRecycler.listOfItems = referralAmountDetailsList
                                dialog.dismissLoadingDialog()
                            }
                            dialog.dismissLoadingDialog()
                        } ?: run {
                            dialog.dismissLoadingDialog()
                        }
                    }
                    Status.ERROR -> {
                        dialog.dismissLoadingDialog()
                        if (response.data?.status != 200) {
                            response.message?.let { it1 -> showErrorDialog(it1) }
                        } else {
                            showErrorDialog(getString(R.string.try_again))
                        }
                    }
                }
            })
        }

    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.view_details), false)
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
            builder.create()
        }
        alertDialog.show()
    }
}