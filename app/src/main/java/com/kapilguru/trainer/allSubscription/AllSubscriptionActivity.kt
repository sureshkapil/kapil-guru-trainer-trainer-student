package com.kapilguru.trainer.allSubscription

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.trainer.*
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.BestTrainerSubscriptionListActivity
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPackageData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.allSubscription.packageSubscription.PackageSubscriptionListActivity
import com.kapilguru.trainer.allSubscription.positionSubscription.PositionSubscriptionListActivity
import com.kapilguru.trainer.databinding.ActivitySubscriptionBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import org.json.JSONArray

class AllSubscriptionActivity : BaseActivity(){
    val TAG = "AllSubscriptionActivity"
    lateinit var viewModel: AllSubscriptionViewModel
    private lateinit var binding: ActivitySubscriptionBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var pageAdapter: SubscriptionsFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription)
        setCustomActionBarListener()
        viewModel = ViewModelProvider(this, AllSubscriptionViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(AllSubscriptionViewModel::class.java)
        binding.model = viewModel
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        setFragmentAdapter()
        addTabSelectListener()
        addTabs()
        registerOnPageChangeCallBacks()
        observeViewModelData()
    }


    private fun setFragmentAdapter() {
        pageAdapter = SubscriptionsFragmentAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = pageAdapter
    }

    private fun addTabSelectListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tabBackGroundColors(tab, false)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabBackGroundColors(tab, true)
                binding.viewPager.currentItem = tab!!.position
            }

            private fun tabBackGroundColors(tab: TabLayout.Tab?, selected: Boolean) {
                val view = tab?.customView
                val varView = view?.findViewById<CardView>(R.id.cardView)
                val headerTitle = view?.findViewById<TextView>(R.id.tv_title)
                view?.let {
                    if (selected) {
                        varView!!.background.setTint(Color.BLUE)
                        headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                    } else {
                        varView!!.background.setTint(Color.WHITE)
                        headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.black))
                    }
                }
            }
        })
    }

    private fun addTabs() {
        for (i in 0 until pageAdapter.itemCount) {
            val tab = binding.tabLayout.newTab().setCustomView(pageAdapter.setCustomTabView(i))
            binding.tabLayout.addTab(tab)
            if (i == 0) {
                binding.tabLayout.selectTab(tab)
            }
        }
    }

    private fun registerOnPageChangeCallBacks() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.subscriptions))
    }

    private fun observeViewModelData() {
        viewModel.getAllSubscriptions.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.let {
                        viewModel.allSubscriptionsList.value = it.allSubscriptionsData as ArrayList<AllSubscriptionsData>
                        viewModel.packageSubscriptionList.addAll(it.allSubscriptionsData.filter {
                            it.subscriptionSubType.equals("package")
                        })
                        viewModel.positionSubscriptionList.addAll(it.allSubscriptionsData.filter {
                            it.subscriptionSubType.equals("position")
                        })
                        viewModel.bestTrainerSubscriptionList.addAll(it.allSubscriptionsData.filter {
                            it.subscriptionSubType.equals("badge")
                        })
                    }
                    val trainerId = StorePreferences(this).userId.toString()
                    viewModel.getMySubscriptionList(trainerId)
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }

            }
        })
        observeMySubscriptionResponse()
    }

    private fun observeMySubscriptionResponse() {
        viewModel.mySubscriptionResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
//                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        viewModel.myPackageSubscriptionList.value =
                            parsePackageSubscription(it.mySubscriptionData.packages)
                        viewModel.myPositionSubscriptionList.value =
                            parsePositionSubscription(it.mySubscriptionData.positions)
                        viewModel.myBestTrainerSubscriptionList.value =
                            parseBestTrainerSubscription(it.mySubscriptionData.badges)
                        progressDialog.dismissLoadingDialog()
                    }

                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun parsePackageSubscription(packageList: String?): ArrayList<MyPackageData> {
        val myPackageList = ArrayList<MyPackageData>()
        packageList?.let { packageListNotNull ->
            val packageJSONArray = JSONArray(packageListNotNull)
            for (i in 0 until packageJSONArray.length()) {
                val packageJSONObject = packageJSONArray.getJSONObject(i)
                val myPackageData = MyPackageData().getMyPackageObject(packageJSONObject)
                myPackageList.add(myPackageData)
            }
        }
        return myPackageList
    }

    private fun parsePositionSubscription(positionList: String?): ArrayList<MyPositionData> {
        val myPositionList = ArrayList<MyPositionData>()
        positionList?.let{
            val positionJSONArray = JSONArray(it)
            for (i in 0 until positionJSONArray.length()) {
                val positionJSONObject = positionJSONArray.getJSONObject(i)
                val myPositionData = MyPositionData().parsePositiondata(positionJSONObject)
                myPositionList.add(myPositionData)
            }
        }
        return myPositionList
    }

    private fun parseBestTrainerSubscription(bestTrainerList: String?): ArrayList<MyBestTrainerData> {
        val myBestTrainerList = ArrayList<MyBestTrainerData>()
        bestTrainerList?.let{
            val bestTrainerJsonArray = JSONArray(it)
            for (i in 0 until bestTrainerJsonArray.length()) {
                val bestTrainerJsonObject = bestTrainerJsonArray.getJSONObject(i)
                val myBestTrainerData = MyBestTrainerData().parseBestTrainer(bestTrainerJsonObject)
                myBestTrainerList.add(myBestTrainerData)
            }
        }
        return myBestTrainerList
    }

    fun launchPositionSubscriptionListActivity(subscriptionsData: AllSubscriptionsData){
        val intent = Intent(this, PositionSubscriptionListActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelableArrayList(PositionSubscriptionListActivity.MY_POSITION_SUBSCRIPTION_LIST,viewModel.myPositionSubscriptionList.value)
        bundle.putParcelable(PositionSubscriptionListActivity.ALL_SUBSCRIPTION_DATA, subscriptionsData)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun launchPackageSubscriptionListActivity() {
        val intent = Intent(this, PackageSubscriptionListActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelableArrayList(PackageSubscriptionListActivity.PACKAGE_SUBSCRIPTION_LIST, viewModel.packageSubscriptionList)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun launchBestTrainerSubscriptionListActivity(){
        val intent = Intent(this, BestTrainerSubscriptionListActivity::class.java)
        val bundle = Bundle()
        val badgeSubscription = viewModel.allSubscriptionsList.value?.filter{
            it.subscriptionSubType?.equals("badge")!!
        }
        bundle.putParcelableArrayList(BestTrainerSubscriptionListActivity.MY_BEST_TRAINER_LIST, viewModel.myBestTrainerSubscriptionList.value)
        bundle.putParcelable(BestTrainerSubscriptionListActivity.BADGE_DATA, badgeSubscription?.get(0))
        intent.putExtras(bundle)
        startActivity(intent)
    }
}