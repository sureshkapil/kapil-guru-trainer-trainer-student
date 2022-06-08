package com.kapilguru.trainer.allSubscription.positionSubscription

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.allSubscription.positionSubscription.confirmation.PositionSubscriptionConfirmActivity
import com.kapilguru.trainer.allSubscription.positionSubscription.model.CoursePositionMapRequest
import com.kapilguru.trainer.allSubscription.positionSubscription.model.TrainerCourseData
import com.kapilguru.trainer.allSubscription.positionSubscription.viewModel.PositionSubscriptionVMFactory
import com.kapilguru.trainer.allSubscription.positionSubscription.viewModel.PositionSubscriptionViewModel
import com.kapilguru.trainer.databinding.ActivityPositionSubscriptionBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import org.json.JSONArray
import org.json.JSONObject

class PositionSubscriptionListActivity : BaseActivity(), PositionSubscriptionListAdapter.PositionItemClickListener {
    private val TAG = "PosSubscListActivity"
    lateinit var binding: ActivityPositionSubscriptionBinding
    lateinit var viewModel: PositionSubscriptionViewModel
    lateinit var adapter: PositionSubscriptionListAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_position_subscription)
        binding.lifecycleOwner = this
        setCustomActionBarListener()
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, PositionSubscriptionVMFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(PositionSubscriptionViewModel::class.java)
        setAdapter()
        getIntentData()
        observeViewModelData()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.position_subscription))
    }

    private fun setAdapter() {
        adapter = PositionSubscriptionListAdapter(applicationContext, this)
        binding.rvPositionList.adapter = adapter
    }

    private fun getIntentData() {
        viewModel.allSubscriptionsData = intent.getParcelableExtra(ALL_SUBSCRIPTION_DATA)!!
        viewModel.myPositionSubscriptionList = intent.getParcelableArrayListExtra<MyPositionData>(MY_POSITION_SUBSCRIPTION_LIST) as ArrayList<MyPositionData>
    }

    private fun observeViewModelData() {
        viewModel.positionSubscriptionListResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        val positionSubscriptionList = getPositionSubscriptionListToShow(it.allData.courses, it.allData.owned, it.allData.occupied)
                        addMySubscriptionDataToList(positionSubscriptionList)
                        viewModel.positionSubscriptionList.addAll(positionSubscriptionList)
                        adapter.setPositionSubscriptionList(positionSubscriptionList)
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
        observeCoursePositionMapResponse()
    }

    private fun observeCoursePositionMapResponse() {
        viewModel.coursePositionMapResponse.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    startActivity(intent)
                    finish()
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    /*add owned data to the list course list*/
    private fun getPositionSubscriptionListToShow(courseList: String?, ownedList: String?, occupiedData: String?): ArrayList<TrainerCourseData> {
        val positionSubscriptionList = ArrayList<TrainerCourseData>()
        courseList?.let { courseListNotNull ->
            val courseJsonArray = JSONArray(courseListNotNull)
            ownedList?.let { ownedListNotNull ->
                val ownedJsonArray = JSONArray(ownedListNotNull)
                for (i in 0 until courseJsonArray.length()) {
                    val trainerCourseObject = getTrainerCourseObject(courseJsonArray.getJSONObject(i))
                    for (j in 0 until ownedJsonArray.length()) {
                        val ownedJsonObject = ownedJsonArray.getJSONObject(j)
                        if (ownedJsonObject.getInt("course_id").equals(trainerCourseObject.id)) {
                            trainerCourseObject.isOwned = true
                            trainerCourseObject.ownedPosition = ownedJsonObject.getInt("course_position_num")
                            break
                        }
                    }
                    positionSubscriptionList.add(trainerCourseObject)
                }
            }
        }
        return addOccupiedPositionData(positionSubscriptionList, occupiedData)
    }

    private fun getTrainerCourseObject(trainerCourseJsonObject: JSONObject): TrainerCourseData {
        val trainerCourseObject = TrainerCourseData(0, "", 0, "")
        if (trainerCourseJsonObject.has("id")) {
            trainerCourseObject.id = trainerCourseJsonObject.getInt("id")
        }
        if (trainerCourseJsonObject.has("course_title")) {
            trainerCourseObject.courseTitle = trainerCourseJsonObject.getString("course_title")
        }
        if (trainerCourseJsonObject.has("trainer_id")) {
            trainerCourseObject.trainerId = trainerCourseJsonObject.getInt("trainer_id")
        }
        if (trainerCourseJsonObject.has("trainer_name")) {
            trainerCourseObject.trainerName = trainerCourseJsonObject.getString("trainer_name")
        }
        return trainerCourseObject
    }

    /*add occupied data to the course list*/
    private fun addOccupiedPositionData(courseList: ArrayList<TrainerCourseData>, occupiedData: String?): ArrayList<TrainerCourseData> {
        if (TextUtils.isEmpty(occupiedData)) return courseList
        occupiedData?.let { occupiedDataNotNull ->
            val occupiedJsonArray = JSONArray(occupiedDataNotNull)
            for (i in 0 until occupiedJsonArray.length()) {
                val occupiedJsonObject = occupiedJsonArray.getJSONObject(i)
                for (course in courseList) {
                    if (course.isOwned) continue
                    if (occupiedJsonObject.getInt("course_id").equals(course.id)) {
                        if (occupiedJsonObject.has("occupied")) {
                            val occupiedString = occupiedJsonObject.getString("occupied")
                            occupiedString.let {
                                if (TextUtils.isEmpty(occupiedString)) {
                                    val occupiedPosJsonArray = JSONArray(occupiedString)
                                    for (occupiedPosIndex in 0 until occupiedPosJsonArray.length()) {
                                        when (occupiedPosJsonArray.get(occupiedPosIndex)) {
                                            1 -> course.isPosition_1_Occupied = true
                                            2 -> course.isPosition_2_Occupied = true
                                            3 -> course.isPosition_3_Occupied = true
                                            4 -> course.isPosition_4_Occupied = true
                                            5 -> course.isPosition_5_Occupied = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return courseList
    }

    private fun addMySubscriptionDataToList(courseList: ArrayList<TrainerCourseData>) {
        for (myPosSubs in viewModel.myPositionSubscriptionList) {
            for (course in courseList) {
                if (myPosSubs.courseId == course.id) {
                    course.myPosSubs = myPosSubs
                    break
                }
            }
        }
    }

    override fun onSubscribeOrRenewClicked(trainerCourseData: TrainerCourseData) {
        PositionSubscriptionConfirmActivity.launchActivity(this, trainerCourseData,viewModel.allSubscriptionsData)
//        mapCoursePosition(trainerCourseData)
    }

    private fun mapCoursePosition(course: TrainerCourseData) {
        var courseExpiryDate = ""
        var courseStartDate = ""
        Log.d(TAG, "mapCoursePosition TrainerCourseData : " + course)
        course.myPosSubs?.let {
            it.expiryDate?.let { expiryDate ->
                courseExpiryDate = expiryDate
            }
            it.startDate?.let { startDate ->
                courseStartDate = startDate
            }
        }
        val coursePositionMapRequest =
            CoursePositionMapRequest(course.id, course.positionToSubscribe, courseExpiryDate, courseStartDate, course.trainerId)
        Log.d(TAG, "mapCoursePosition : " + coursePositionMapRequest)
//        viewModel.mapCoursePosition(coursePositionMapRequest)
    }

    companion object {
        val ALL_SUBSCRIPTION_DATA = "AllSubscriptionsData"
        val MY_POSITION_SUBSCRIPTION_LIST = "my_position_subscription_list"
    }
}