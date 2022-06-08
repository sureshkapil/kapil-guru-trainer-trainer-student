package com.kapilguru.trainer.ui.earnings

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.VIEW_SELECTED_LOCKED_AMOUNT
import com.kapilguru.trainer.databinding.ActivityEarningsDetailsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.earnings.adapter.EarningsDetailsFragmentAdapter
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsCourse
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsReferral
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsWebinar
import com.kapilguru.trainer.ui.earnings.viewModel.EarningsDetailsViewModel
import com.kapilguru.trainer.ui.earnings.viewModel.EarningsDetailsViewModelFactory
import okhttp3.internal.notifyAll
import org.json.JSONArray
import org.json.JSONObject


class EarningsDetailsActivity : AppCompatActivity() {
    lateinit var earningsDetailsBinding: ActivityEarningsDetailsBinding
    lateinit var earningsDetailsViewModel: EarningsDetailsViewModel
    lateinit var earningsDetailsFragmentAdapter: EarningsDetailsFragmentAdapter
    lateinit var dialog: CustomProgressDialog

    companion object {
        private const val TAG = "EarningsDetailsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        earningsDetailsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_earnings_details
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.locked_amount)

        earningsDetailsViewModel = ViewModelProvider(
            this, EarningsDetailsViewModelFactory(
                ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),
                application
            )
        ).get(EarningsDetailsViewModel::class.java)


        earningsDetailsBinding.detailsViewModel = earningsDetailsViewModel
        earningsDetailsBinding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)

        val tablayout = earningsDetailsBinding.tabLayout

        setPageAdapter()
        setTabAndListener(tablayout)
        getSelectedIndex(tablayout)

        earningsDetailsBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tablayout.selectTab(tablayout.getTabAt(position))
            }
        })
        observeViewModelData()
    }

    private fun getSelectedIndex(tablayout: TabLayout) {
        val index = intent.getIntExtra(VIEW_SELECTED_LOCKED_AMOUNT,0)
        earningsDetailsBinding.viewPager.currentItem = index
        tablayout.getTabAt(index)?.select()
    }

    private fun setPageAdapter() {
        val fragmentManager = supportFragmentManager

        earningsDetailsFragmentAdapter = EarningsDetailsFragmentAdapter(fragmentManager, lifecycle)

        earningsDetailsBinding.viewPager.adapter = earningsDetailsFragmentAdapter
    }

    private fun setTabAndListener(tablayout: TabLayout) {
        tablayout.addTab(tablayout.newTab().setText(getString(R.string.course_amount)))
        tablayout.addTab(tablayout.newTab().setText(getString(R.string.referral_amount)))
        tablayout.addTab(tablayout.newTab().setText(getString(R.string.webinar_amount)))

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                earningsDetailsBinding.viewPager.currentItem = tab!!.position
            }

        })
    }

    private fun observeViewModelData() {
        earningsDetailsViewModel.earningsDetailsListApi.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.let {
                        earningsDetailsViewModel.earningsDetailsApiData.value = it.apiAllData
                        earningsDetailsViewModel.jsonObjectString.value = it.apiAllData.expected
                        val json = JSONObject(it.apiAllData.expected)
                        val coursesStr = json.getString("courses")
                        val referralsStr = json.getString("referrals")
                        val webinarStr = json.getString("webinars")

                        val coursesArray = JSONArray(coursesStr)
                        for (i in 0 until coursesArray.length()) {
                            val jsonstr = coursesArray.getJSONObject(i)

                            val insertData = EarningsDetailsCourse().apply {
                                batchId = jsonstr.getInt("batch_id")
                                studentId = jsonstr.getInt("student_id")
                                studentName = jsonstr.getString("student_name")
                                enrollmentDate = jsonstr.getString("enrollment_date")
                                totalFee = jsonstr.getDouble("total_fee")
                                startDate = jsonstr.getString("start_date")
                                endDate = jsonstr.getString("end_date")
                            }
                            earningsDetailsViewModel.earningsDetailsData.postValue(
                                arrayListOf(
                                    insertData
                                )
                            )
                        }

                        val referralsArray = JSONArray(referralsStr)
                        val listOfReferrals = ArrayList<EarningsDetailsReferral>()
                        for (j in 0 until referralsArray.length()) {
                            val jsonReferralString = referralsArray.getJSONObject(j)

                            val insertReferralData = EarningsDetailsReferral().apply {
                                userId = jsonReferralString.getInt("user_id")
                                referralId = jsonReferralString.optInt("referral_id", 0)
                                referralName = jsonReferralString.getString("referral_name")
                                referredDate = jsonReferralString.getString("referred_date")
                                referredTo = jsonReferralString.getString("referred_to")
                                subscriptionDate =
                                    jsonReferralString.getString("subscription_date")
                                subscriptionAmount =
                                    jsonReferralString.optInt("subscription_amount", 0)
                                referralAmount = jsonReferralString.getInt("referral_amount")

                            }
                            listOfReferrals.add(insertReferralData)
                        }
                        earningsDetailsViewModel.earningsDetailsReferrals.postValue(listOfReferrals)

                        val webinarArray = JSONArray(webinarStr)
                        val listOfWebinar = ArrayList<EarningsDetailsWebinar>()
                        for (j in 0 until webinarArray.length()) {
                            val jsonWebinarString = webinarArray.getJSONObject(j)
                            val insertWebinarData = EarningsDetailsWebinar().apply {
                                userId = jsonWebinarString.getInt("user_id")
                                webinarId = jsonWebinarString.getInt("webinar_id")
                                title = jsonWebinarString.getString("title")
                                startDate = jsonWebinarString.optString("start_date", "0")
                                attendeeId = jsonWebinarString.getInt("attendee_id")
                                attendeeName = jsonWebinarString.getString("attendee_name")
                                enrollmentDate = jsonWebinarString.optString("enrollment_date", "0")
                                fee = jsonWebinarString.optInt("fee", 0)

                            }
                            listOfWebinar.add(insertWebinarData)
                        }
                        earningsDetailsViewModel.earningsDetailsWebinar.postValue(listOfWebinar)


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

}