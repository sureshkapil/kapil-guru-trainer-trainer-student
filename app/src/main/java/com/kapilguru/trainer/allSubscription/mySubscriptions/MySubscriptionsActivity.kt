package com.kapilguru.trainer.allSubscription.mySubscriptions

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPackageData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.allSubscription.mySubscriptions.viewModel.MySubscriptionViewModel
import com.kapilguru.trainer.allSubscription.mySubscriptions.viewModel.MySubscriptionViewModelFactory
import com.kapilguru.trainer.databinding.ActivityMySubscriptionsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import org.json.JSONArray

class MySubscriptionsActivity : AppCompatActivity() {
    val TAG = "MySubscriptionsActivity"
    lateinit var binding: ActivityMySubscriptionsBinding
    lateinit var fragmentAdapter: MySubscriptionsFragmentAdapter
    lateinit var viewModel: MySubscriptionViewModel
    lateinit var progressDialog : CustomProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_subscriptions)
        setToolBar()
        addTabs()
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this,MySubscriptionViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),application))
            .get(MySubscriptionViewModel::class.java)
        getIntentData()
//        observeViewModelData()
    }

    private fun setToolBar(){
        supportActionBar?.title = "My Subscriptions"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun addTabs() {
        val fragmentManager = supportFragmentManager
        fragmentAdapter = MySubscriptionsFragmentAdapter(fragmentManager, lifecycle)
        binding.viewPager.adapter = fragmentAdapter
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Package"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Position"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Best Trainer"))
        addTabSelectListener()
    }

    private fun addTabSelectListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.v(TAG, tab!!.position.toString())
                binding.viewPager.currentItem = tab.position
            }
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun getIntentData(){
        viewModel.myPackageSubscriptionList.value = intent.getParcelableArrayListExtra(MY_PACKAGE_LIST)
        viewModel.myPositionSubscriptionList.value = intent.getParcelableArrayListExtra(MY_POSITION_LIST)
        viewModel.myBestTrainerSubscriptionList.value = intent.getParcelableArrayListExtra<MyBestTrainerData>(MY_BEST_TRAINER_LIST)
    }

    // we are passding the data through intent. No need to call the api.
/*
    private fun observeViewModelData(){
        viewModel.mySubscriptionResponse.observe(this, Observer {
            when(it.status){
                Status.LOADING ->{
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        Log.d(TAG,"onSuccess")
                        viewModel.myPackageSubscriptionList.value = parsePackageSubscuription(it.mySubscriptionData.packages)
                        viewModel.myPositionSubscriptionList.value = parsePositionSubscuription(it.mySubscriptionData.positions)
                        viewModel.myBestTrainerSubscriptionList.value= parseBestTrainerSubscuription(it.mySubscriptionData.badges)
                        progressDialog.dismissLoadingDialog()
                    }
                }
                Status.ERROR->{
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }
*/

    private fun parsePackageSubscuription(packageList : String) : ArrayList<MyPackageData>{
        val myPackageList = ArrayList<MyPackageData>()
        val packageJSONArray = JSONArray(packageList)
        for(i in 0 until packageJSONArray.length()){
            val packageJSONObject = packageJSONArray.getJSONObject(i)
            val myPackageData = MyPackageData().getMyPackageObject(packageJSONObject)
            myPackageList.add(myPackageData)
        }
        return myPackageList
    }

    private fun parsePositionSubscuription(positionList : String) : ArrayList<MyPositionData>{
        val myPositionList = ArrayList<MyPositionData>()
        val positionJSONArray = JSONArray(positionList)
        for(i in 0 until positionJSONArray.length()){
            val positionJSONObject = positionJSONArray.getJSONObject(i)
            val myPositionData = MyPositionData().parsePositiondata(positionJSONObject)
            myPositionList.add(myPositionData)
        }
        return myPositionList
    }

    private fun parseBestTrainerSubscuription(bestTrainerList : String) : ArrayList<MyBestTrainerData>{
        val myBestTrainerList = ArrayList<MyBestTrainerData>()
        val bestTrainerJsonArray = JSONArray(bestTrainerList)
        for(i in 0 until bestTrainerJsonArray.length()){
            val bestTrainerJsonObject = bestTrainerJsonArray.getJSONObject(i)
            val myBestTrainerData = MyBestTrainerData().parseBestTrainer(bestTrainerJsonObject)
            myBestTrainerList.add(myBestTrainerData)
        }
        return myBestTrainerList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                onBackPressed()
                true
            }else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object{
        val MY_PACKAGE_LIST = "my_package_list"
        val MY_POSITION_LIST = "my_position_list"
        val MY_BEST_TRAINER_LIST = "my_best_trainer_list"
    }
}