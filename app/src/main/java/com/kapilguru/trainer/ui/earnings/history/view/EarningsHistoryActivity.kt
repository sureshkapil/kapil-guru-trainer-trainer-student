package com.kapilguru.trainer.ui.earnings.history.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityEarningsHistoryBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.earnings.amountDetails.AmountViewDetails
import com.kapilguru.trainer.ui.earnings.history.model.PaidBankDetails
import com.kapilguru.trainer.ui.earnings.history.viewModels.EarningsHistoryViewModel
import com.kapilguru.trainer.ui.earnings.history.viewModels.EarningsHistoryViewModelFactory
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsCourse
import com.kapilguru.trainer.ui.earnings.referralDetails.ReferralAmount
import com.kapilguru.trainer.ui.earnings.referralDetails.ReferralAmountDetails
import com.kapilguru.trainer.ui.earnings.webinarAmount.WebinarAmount
import com.kapilguru.trainer.ui.earnings.webinarAmount.WebinarAmountResponse
import org.json.JSONArray

class EarningsHistoryActivity : BaseActivity(),
    EarningsDetailsHistoryAdapter.EarningsDetailsHistoryAdapterListener {

    lateinit var binding: ActivityEarningsHistoryBinding
    lateinit var viewModel: EarningsHistoryViewModel
    lateinit var dialog: CustomProgressDialog
    lateinit var adapter: EarningsDetailsHistoryAdapter
    private  val TAG = "EarningsHistoryActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_earnings_history)
        setActionBar()

        viewModel = ViewModelProvider(
                this, EarningsHistoryViewModelFactory(
                    ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),
                    application)).get(EarningsHistoryViewModel::class.java)

        dialog = CustomProgressDialog(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = EarningsDetailsHistoryAdapter(this,this )
        binding.recyclerview.adapter = adapter
        viewModel.fetchResponse(StorePreferences(this).trainerId.toString())
        viewModelsObservers()

    }

    fun setActionBar() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.earnings),false)
    }

    private fun viewModelsObservers() {
        viewModel.resultInfo.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.let { earningsListApi ->
//                        Log.d(TAG, "viewModelsObservers: ${earningsListApi.earningsHistoryResponse?.size}")
                        adapter.setData(earningsListApi.earningsHistoryResponse)
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    if (response.data?.status != 200) {
//                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
//                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }
        })

        viewModel.paymentInfo.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.let { paymentAmountInfo ->
                        val paymentInfo: String? = paymentAmountInfo.historyPaymentAmountDetailsResponse.payment
                        adapter.paidBankDetails.value = PaidBankDetails().getPaidBankDetails(paymentInfo)
                        // set Courses
                        val courseInfo: String? = paymentAmountInfo.historyPaymentAmountDetailsResponse.courses
                        courseInfo?.let {
                            val coursesArray = JSONArray(courseInfo)
                            val courseAmountDetailsList = arrayListOf<EarningsDetailsCourse>()
                            for (i in 0 until coursesArray.length()) {
                                val jsonstr = coursesArray.getJSONObject(i)
                                courseAmountDetailsList.add(EarningsDetailsCourse().getEarningsCourseDetails(jsonstr))
                            }
                            viewModel.earningsDetailsCourse = courseAmountDetailsList
                        }


                        // set referrals
                        val referrals: String? = paymentAmountInfo.historyPaymentAmountDetailsResponse.referrals
                        referrals?.let {
                            val referralsArray = JSONArray(referrals)
                            val referralAmountDetailsList = arrayListOf<ReferralAmount>()
                            for (i in 0 until referralsArray.length()) {
                                val jsonstr = referralsArray.getJSONObject(i)
                                referralAmountDetailsList.add(ReferralAmount().getReferralAmountDetails(jsonstr))
                            }
                            viewModel.referralAmountDetails = referralAmountDetailsList
                        }


                        // set webinar
                        val webinars: String? = paymentAmountInfo.historyPaymentAmountDetailsResponse.webinars
                        webinars?.let {
                            val webinarsArray = JSONArray(webinars)
                            val webinarAmountDetailsList = arrayListOf<WebinarAmountResponse>()
                            for (i in 0 until webinarsArray.length()) {
                                val jsonstr = webinarsArray.getJSONObject(i)
                                webinarAmountDetailsList.add(WebinarAmountResponse().getWebinarAmountDetails(jsonstr))
                            }
                            viewModel.webinarAmountDetails = webinarAmountDetailsList
                        }


                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    if (response.data?.status != 200) {
//                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
//                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getSelectedId(id: String) {
        val pref = StorePreferences(application)
        viewModel.fetchHistoryDetailsAmount(trainerId =  pref.trainerId.toString(),selectedId = id)
    }

    override fun onCourseAmountClick() {
        startActivity(Intent(this, AmountViewDetails::class.java).putExtra(PARAM_IS_FROM, PARAM_IS_FROM_EARNINGS_HISTORY).putParcelableArrayListExtra(PARAM_AMOUNT_LIST_INFO,viewModel.earningsDetailsCourse))
    }

    override fun onReferralAmountClick() {
        startActivity(Intent(this, ReferralAmountDetails::class.java).putExtra(PARAM_IS_FROM, PARAM_IS_FROM_EARNINGS_HISTORY).putParcelableArrayListExtra(PARAM_AMOUNT_LIST_INFO,viewModel.referralAmountDetails))
    }

    override fun onWebinarAmountClick() {
        startActivity(Intent(this, WebinarAmount::class.java).putExtra(PARAM_IS_FROM, PARAM_IS_FROM_EARNINGS_HISTORY).putParcelableArrayListExtra(PARAM_AMOUNT_LIST_INFO,viewModel.webinarAmountDetails))
    }
}