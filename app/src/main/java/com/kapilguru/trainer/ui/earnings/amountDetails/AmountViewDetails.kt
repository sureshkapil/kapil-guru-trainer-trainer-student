package com.kapilguru.trainer.ui.earnings.amountDetails

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityAmountViewDetailsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsCourse
import kotlinx.android.synthetic.main.activity_amount_view_details.*
import org.json.JSONArray
import org.json.JSONObject

class AmountViewDetails : BaseActivity() {

    lateinit var binding: ActivityAmountViewDetailsBinding
    lateinit var viewModel: AmountViewModel
    lateinit var dialog: CustomProgressDialog
    lateinit var amountDetailsRecycler: AmountDetailsRecycler
    private val TAG = "AmountViewDetails"
    var shouldReadAvailable: Boolean = true
    var isFromHistory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_amount_view_details)
        viewModel = ViewModelProvider(
            this, AmountViewModelFactory(
                ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application
            )
        ).get(AmountViewModel::class.java)

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
        amountDetailsRecycler = AmountDetailsRecycler()
        recycler.adapter = amountDetailsRecycler
    }

    private fun viewModelObserver() {

        isFromHistory?.let {
            val data  = intent.getParcelableArrayListExtra<EarningsDetailsCourse>(PARAM_AMOUNT_LIST_INFO)
            data?.let {
                amountDetailsRecycler.listOFEarningsDetailsCourse = data
            }
        }?: kotlin.run {
            viewModel.earningsDetailsListApi.observe(this, Observer { response ->

                when (response.status) {

                    Status.LOADING -> {
                        dialog.showLoadingDialog()
                    }

                    Status.SUCCESS -> {
                        response.data?.let {
                            val json: JSONObject
                            var coursesStr = ""
                            when (shouldReadAvailable) {
                                true -> {
                                    json = JSONObject(it.apiAllData.available)
                                    coursesStr = json.getString("courses")
                                }
                                else -> {
                                    json = JSONObject(it.apiAllData.expected)
                                    coursesStr = json.getString("courses")
                                }
                            }
                            val coursesArray = JSONArray(coursesStr)
                            val courseAmountDetailsList = arrayListOf<EarningsDetailsCourse>()
                            for (i in 0 until coursesArray.length()) {
                                val jsonstr = coursesArray.getJSONObject(i)
                                courseAmountDetailsList.add(EarningsDetailsCourse().getEarningsCourseDetails(jsonstr))
                            }
                            amountDetailsRecycler.listOFEarningsDetailsCourse = courseAmountDetailsList

                        }
                        dialog.dismissLoadingDialog()
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
            // Create the AlertDialog
            builder.create()
        }
        alertDialog.show()
    }

}