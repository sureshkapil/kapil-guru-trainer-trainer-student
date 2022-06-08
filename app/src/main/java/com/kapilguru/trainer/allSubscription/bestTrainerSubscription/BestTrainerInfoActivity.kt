package com.kapilguru.trainer.allSubscription.bestTrainerSubscription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.AllSubscriptionViewModel
import com.kapilguru.trainer.allSubscription.AllSubscriptionViewModelFactory
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.packageSubscription.view.SubscriptionInfoTextView
import com.kapilguru.trainer.allSubscription.positionSubscription.PositionSubscriptionListActivity
import com.kapilguru.trainer.databinding.ActivityBestTrainerInfoBinding
import com.kapilguru.trainer.network.RetrofitNetwork

class BestTrainerInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityBestTrainerInfoBinding
    lateinit var viewModel: AllSubscriptionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_best_trainer_info)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(
            this,
            AllSubscriptionViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        )
            .get(AllSubscriptionViewModel::class.java)
        setSupportActionBar()
        viewModel.myBestTrainerSubscriptionList.value = intent.getParcelableArrayListExtra(MY_BEST_TRAINER_LIST)
        setPositionSubscriptionInfoText()
        setClickListeners()
    }

    private fun setSupportActionBar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.Best_trainer_info)
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
            launchBestTrainerSubscriptionListActivity(viewModel.positionSubscriptionList)
        }
    }
    private fun launchBestTrainerSubscriptionListActivity(positionSubscriptionList : ArrayList<AllSubscriptionsData>){
        val intent = Intent(this, BestTrainerSubscriptionListActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelableArrayList(BestTrainerSubscriptionListActivity.MY_BEST_TRAINER_LIST, viewModel.myBestTrainerSubscriptionList.value)
        intent.putExtras(bundle)
        startActivity(intent)
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
        val BEST_TRAINER_LIST = "best_trainer_list"
        val MY_BEST_TRAINER_LIST = "my_best_trainer_list"
    }
}