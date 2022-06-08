package com.kapilguru.trainer.allSubscription.positionSubscription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.AllSubscriptionViewModel
import com.kapilguru.trainer.allSubscription.AllSubscriptionViewModelFactory
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.allSubscription.packageSubscription.view.SubscriptionInfoTextView
import com.kapilguru.trainer.databinding.ActivityPositionSubscriptionInfoBinding
import com.kapilguru.trainer.network.RetrofitNetwork

class PositionSubscriptionInfoActivity : AppCompatActivity() {
    private val TAG= "PositionSubscriptionInfoActivity"
    lateinit var binding : ActivityPositionSubscriptionInfoBinding
    lateinit var viewModel : AllSubscriptionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_position_subscription_info)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, AllSubscriptionViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(AllSubscriptionViewModel::class.java)
        setSupportActionBar()
        getIntentData()
        setPositionSubscriptionInfoText()
        setClickListeners()
    }

    private fun setSupportActionBar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.position_info)
    }
    private fun getIntentData(){
        intent.getParcelableArrayListExtra<AllSubscriptionsData>(POSITION_LIST)?.let{
            viewModel.positionSubscriptionList = it
        }
        viewModel.myPositionSubscriptionList.value = intent.getParcelableArrayListExtra(MY_POSITION_LIST)
    }

    private fun setPositionSubscriptionInfoText(){
        val textView = SubscriptionInfoTextView(binding.llTextviews,this)
        textView.setText("suresh")
        val textView2 = SubscriptionInfoTextView(binding.llTextviews,this)
        textView2.setText("Gunda")
        val textView3 = SubscriptionInfoTextView(binding.llTextviews,this)
        textView3.setText("Gunda")
        val textView4 = SubscriptionInfoTextView(binding.llTextviews,this)
        textView4.setText("Gunda")
        val textView5 = SubscriptionInfoTextView(binding.llTextviews,this)
        textView5.setText("Gunda")
    }

    private fun setClickListeners(){
        binding.actvRegister.setOnClickListener {
        }
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
        val POSITION_LIST = "position_list"
        val MY_POSITION_LIST = "position_list"
    }
}