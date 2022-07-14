package com.kapilguru.trainer.ui.earnings

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityEarningsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.earnings.amountDetails.AmountViewDetails
import com.kapilguru.trainer.ui.earnings.earningsDetails.NewTrainerEarningsDetailsActivity
import com.kapilguru.trainer.ui.earnings.history.view.EarningsHistoryActivity
import com.kapilguru.trainer.ui.earnings.viewModel.EarningsViewModel
import com.kapilguru.trainer.ui.earnings.viewModel.EarningsViewModelFactory
import kotlinx.android.synthetic.main.activity_earnings.*

class EarningsActivity : BaseActivity(), EarningsMergerView.ItemClickListener{

    lateinit var earningsBinding: ActivityEarningsBinding
    lateinit var earningsViewModel: EarningsViewModel
    lateinit var dialog: CustomProgressDialog
    private  val TAG = "EarningsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        earningsBinding = DataBindingUtil.setContentView(this, R.layout.activity_earnings)
        earningsBinding.clickListener = this

        setCustomActionBarListener()

        earningsViewModel = ViewModelProvider(
            this,
            EarningsViewModelFactory(
                ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),
                application)).get(EarningsViewModel::class.java)
        dialog = CustomProgressDialog(this)
        earningsBinding.earningsModel = earningsViewModel
        earningsBinding.lifecycleOwner = this

        setClickListeners()
        observeViewModelData()
    }

    private fun setClickListeners() {
        availableAmountView.setClickListener(this)
//        lockedAmountView.setClickListener(this)
        history.setOnClickListener {
//            navigateToHistory()
        }
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, earningsBinding.actionbar, getString(R.string.earnings),false)
    }

    private fun observeViewModelData() {
//        earningsViewModel.earningsListApi.observe(this, Observer {
//            when (it.status) {
//
//                Status.LOADING -> {
//                    dialog.showLoadingDialog()
//                }
//
//                Status.SUCCESS -> {
//                    it.data?.let { earningsListApi ->
//                        earningsViewModel.earningsApiResponse.value =
//                            earningsListApi.earningsListApiData[0]
//                    }
//                    dialog.dismissLoadingDialog()
//                }
//
//                Status.ERROR -> {
//                    dialog.dismissLoadingDialog()
//                    if (it.data?.status != 200) {
//                        it.message?.let { it1 -> showErrorDialog(it1) }
//                    } else {
//                        showErrorDialog(getString(R.string.try_again))
//                    }
//                }
//            }
//        })

        earningsViewModel.earningsDataResponse.observe(this, Observer {
            when (it.status) {

                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.earningsDataResponseApi?.let { info ->
                            if(info.isNotEmpty()) {
                                earningsViewModel.earningsDataResponseApi.value = info[0]
                            }
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    if (it.data?.status != 200) {
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }
        })


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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onCourseClick(view: View, position:Int) {
        val intent = Intent(view.context, EarningsDetailsActivity::class.java)
            intent.putExtra(VIEW_SELECTED_LOCKED_AMOUNT,position)
            startActivity(intent)
    }


    private fun navigateToHistory() {
        startActivity(Intent(this, EarningsHistoryActivity::class.java))
    }

    override fun onItemClick(amountType: AmountType) {

        when(amountType) {
            AmountType.COURSEAMOUNT -> {
//               startActivity(Intent(this,AmountViewDetails::class.java).putExtra(PARAM_IS_AVAILABLE,true))
            }
            AmountType.RECORDEDCOURSES -> {
//                startActivity(Intent(this,AmountViewDetails::class.java).putExtra(PARAM_IS_AVAILABLE,false))
            }
            AmountType.STUDYMATERIAL -> {
//                startActivity(Intent(this, ReferralAmountDetails::class.java).putExtra(PARAM_IS_AVAILABLE,true))
            }
            AmountType.VIEWDETAILS -> {
                startActivity(Intent(this, NewTrainerEarningsDetailsActivity::class.java))
            }
         /*   AmountType.EXPECTEDREFERRALAMOUNT -> {
                startActivity(Intent(this,ReferralAmountDetails::class.java).putExtra(PARAM_IS_AVAILABLE,false))
            }
            AmountType.WEBINARAMOUNT -> {
                startActivity(Intent(this,WebinarAmount::class.java).putExtra(PARAM_IS_AVAILABLE,true))
            }
            AmountType.EXPECTEDWEBINARAMOUNT -> {
                startActivity(Intent(this,WebinarAmount::class.java).putExtra(PARAM_IS_AVAILABLE,false))
            }*/
        }

    }

}