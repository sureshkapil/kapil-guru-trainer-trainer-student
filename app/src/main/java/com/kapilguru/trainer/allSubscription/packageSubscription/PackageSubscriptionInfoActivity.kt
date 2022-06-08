package com.kapilguru.trainer.allSubscription.packageSubscription

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.packageSubscription.view.SubscriptionInfoTextView
import com.kapilguru.trainer.allSubscription.packageSubscription.viewModel.PackageSubscriptionViewModel
import com.kapilguru.trainer.allSubscription.packageSubscription.viewModel.PackageSubscriptionViewModelFactory
import com.kapilguru.trainer.allSubscription.positionSubscription.viewModel.PositionSubscriptionViewModel
import com.kapilguru.trainer.databinding.ActivityPackageSubscriptionInfoBinding
import com.kapilguru.trainer.databinding.ActivityPositionSubscriptionBinding
import com.kapilguru.trainer.fromBase64
import com.kapilguru.trainer.network.RetrofitNetwork
import kotlinx.android.synthetic.main.activity_package_subscription_info.*
import org.json.JSONArray

class PackageSubscriptionInfoActivity : AppCompatActivity() {
    val TAG = "PositionSubInfoActivity"
    lateinit var binding: ActivityPackageSubscriptionInfoBinding
    lateinit var viewModel: PackageSubscriptionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package_subscription_info)
        binding.lifecycleOwner = this
        setSupportActionBar()
        viewModel = ViewModelProvider(
            this,
            PackageSubscriptionViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        ).get(PackageSubscriptionViewModel::class.java)
        setPackageSubscriptionInfo()
    }

    private fun setSupportActionBar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.package_info)
    }

    private fun setPackageSubscriptionInfo(){
     /*   val bundle = intent.extras
       bundle?.let{
           val subscriptionData = it.getParcelable<AllSubscriptionsData>(PACKAGE_SUBCRIPTION_DATA)
           viewModel.selectedPackage = subscriptionData!!
       }
        val decodedsubscription = viewModel.selectedPackage.subscriptionDesc!!.fromBase64()
        val jsonAray = JSONArray(decodedsubscription)
        for(i in 0 until jsonAray.length()){
            val jsonObjet = jsonAray.getJSONObject(i)
           viewModel.descriptionList.add(jsonObjet.getString("subscriptionSubHeading"))
        }
        for(i in 0 until viewModel.descriptionList.size){
            val textView = SubscriptionInfoTextView(binding.llTextviews,this)
            textView.setHtml(viewModel.descriptionList[i].replace("p","b"))
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }else ->{
                return super.onOptionsItemSelected(item)
            }
        }
    }

    companion object{
        val PACKAGE_SUBCRIPTION_DATA = "PACKAGE_SUBSCRIPTION_DATA"
    }
}