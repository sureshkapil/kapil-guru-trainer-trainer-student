package com.kapilguru.trainer.allSubscription.packageSubscription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.packageSubscription.view.PackageSubscriptionListAdapter
import com.kapilguru.trainer.databinding.ActivityPackageSubscriptionListBinding

// not called, directly take user to payment activity
class PackageSubscriptionListActivity : AppCompatActivity(),PackageSubscriptionListAdapter.PackageItemClickListener {
    lateinit var binding: ActivityPackageSubscriptionListBinding
    lateinit var adapter : PackageSubscriptionListAdapter
    var packageSubscriptionList = ArrayList<AllSubscriptionsData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package_subscription_list)
        binding.lifecycleOwner = this
        packageSubscriptionList = intent.getParcelableArrayListExtra<AllSubscriptionsData>(PACKAGE_SUBSCRIPTION_LIST) as ArrayList<AllSubscriptionsData>
        setSupportActionBar()
        setAdapter()
    }

    private fun setSupportActionBar(){
     supportActionBar?.setDisplayHomeAsUpEnabled(true)
     supportActionBar?.title = getString(R.string.package_subscription)
    }

    private fun setAdapter(){
        adapter = PackageSubscriptionListAdapter(this)
        binding.rvPackageList.adapter = adapter
        adapter.setSubscriptionList(packageSubscriptionList)
    }

    companion object {
        val PACKAGE_SUBSCRIPTION_LIST = "Package_Subscription_List"
    }

    override fun onPackageClicked(packageSubscriptionData : AllSubscriptionsData) {
        launchPackageSubscriptionInfoActivity(packageSubscriptionData)
    }

    private fun launchPackageSubscriptionInfoActivity(packageSubscriptionData : AllSubscriptionsData){
        val intent = Intent(this, PackageSubscriptionInfoActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(PackageSubscriptionInfoActivity.PACKAGE_SUBCRIPTION_DATA,packageSubscriptionData)
        intent.putExtras(bundle)
        startActivity(intent)
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