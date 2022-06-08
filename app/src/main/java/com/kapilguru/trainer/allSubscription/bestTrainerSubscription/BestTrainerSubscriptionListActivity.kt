package com.kapilguru.trainer.allSubscription.bestTrainerSubscription

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.confirmation.BadgeSubscriptionConfirmationActivity
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.BestTrainerCourseData
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.viewModel.BestTrainerViewModel
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.viewModel.BestTrainerViewModelFactory
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.databinding.ActivityBestTrainerSubscriptionListBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import org.json.JSONArray
import org.json.JSONObject

class BestTrainerSubscriptionListActivity : BaseActivity(),
    BestTrainerSubscriptionAdapter.ItemClickListener {
    private val TAG = "BestTraSubListActi"
    lateinit var binding: ActivityBestTrainerSubscriptionListBinding
    lateinit var viewModel: BestTrainerViewModel
    lateinit var adapter: BestTrainerSubscriptionAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_best_trainer_subscription_list)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, BestTrainerViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(BestTrainerViewModel::class.java)
        setCustomActionBarListener()
        getIntentData()
        setAdapter()
        observeViewModelData()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.badge_subscription))
    }

    private fun getIntentData() {
        viewModel.myBestTrainerSubscList = intent.getParcelableArrayListExtra<MyBestTrainerData>(MY_BEST_TRAINER_LIST) as ArrayList<MyBestTrainerData>
        viewModel.badgeData = intent.getParcelableExtra(BADGE_DATA)!!
    }

    private fun setAdapter() {
        adapter = BestTrainerSubscriptionAdapter(this)
        binding.rvBestTrainerList.adapter = adapter
    }

    private fun observeViewModelData() {
        viewModel.bestTrainerCourseList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        val bestTrainerCourseList = getBestTrainerCourseList(it.bestTrainerData.bestTrainerCourses, it.bestTrainerData.bestTrainerBadges)
                        addMyBestTrainerData(bestTrainerCourseList)
                        adapter.setBestTrainerCourseList(bestTrainerCourseList)
                        progressDialog.dismissLoadingDialog()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun getBestTrainerCourseList(courseList: String?, badgeList: String?): ArrayList<BestTrainerCourseData> {
        val bestTrainerList = ArrayList<BestTrainerCourseData>()
        courseList?.let { courseListNotNull ->
            val courseJsonArray = JSONArray(courseListNotNull)
            badgeList?.let { badgeListNotNull ->
                val badgeJSONArray = JSONArray(badgeListNotNull)
                for (i in 0 until courseJsonArray.length()) {
                    val bestTrainerObject = getBestTrainerObject(courseJsonArray.getJSONObject(i))
                    for (j in 0 until badgeJSONArray.length()) {
                        val badgeJsonObject = badgeJSONArray.getJSONObject(j)
                        if (badgeJsonObject.getInt("course_id").equals(bestTrainerObject.id)) {
                            bestTrainerObject.isBadgeBought = true
                            break
                        }
                    }
                    bestTrainerList.add(bestTrainerObject)
                }
            }
        }
        return bestTrainerList
    }

    private fun getBestTrainerObject(bestTrainerJsonObject: JSONObject): BestTrainerCourseData {
        val bestTrainerObject = BestTrainerCourseData(0, "", 0, "")
        if (bestTrainerJsonObject.has("id")) {
            bestTrainerObject.id = bestTrainerJsonObject.getInt("id")
        }
        if (bestTrainerJsonObject.has("course_title")) {
            bestTrainerObject.courseTitle = bestTrainerJsonObject.getString("course_title")
        }
        if (bestTrainerJsonObject.has("trainer_id")) {
            bestTrainerObject.trainerId = bestTrainerJsonObject.getInt("trainer_id")
        }
        if (bestTrainerJsonObject.has("trainer_name")) {
            bestTrainerObject.trainerName = bestTrainerJsonObject.getString("trainer_name")
        }
        return bestTrainerObject
    }

    private fun addMyBestTrainerData(bestTrainerCourseList: ArrayList<BestTrainerCourseData>) {
        for (myBestTrainerSub in viewModel.myBestTrainerSubscList) {
            for (bestTrainerCourse in bestTrainerCourseList) {
                if (myBestTrainerSub.courseId == bestTrainerCourse.id) {
                    bestTrainerCourse.myBestTrainerData = myBestTrainerSub
                    break
                }
            }
        }
    }

    companion object {
        val MY_BEST_TRAINER_LIST = "my_best_trainer_list"
        val BADGE_DATA = "all_subscription_data"
    }

    override fun onRenewClicked(courseData: BestTrainerCourseData) {
        BadgeSubscriptionConfirmationActivity.launchActivity(this, viewModel.badgeData, courseData)
    }

    override fun onSubscribeClicked(courseData: BestTrainerCourseData) {
        BadgeSubscriptionConfirmationActivity.launchActivity(this, viewModel.badgeData, courseData)
    }

}