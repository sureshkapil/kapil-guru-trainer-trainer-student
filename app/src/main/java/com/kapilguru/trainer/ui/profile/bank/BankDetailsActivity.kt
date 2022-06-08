package com.kapilguru.trainer.ui.profile.bank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityBankDetailsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.home.HomeActivity
import com.kapilguru.trainer.ui.profile.bank.data.BankDetailsUploadRequest
import com.kapilguru.trainer.ui.profile.bank.viewModel.BankDetailsViewModel
import com.kapilguru.trainer.ui.profile.bank.viewModel.BankDetailsViewModelFactory

class BankDetailsActivity : BaseActivity() {
    private val TAG = "BankDetailsActivity"
    lateinit var viewModel : BankDetailsViewModel
    lateinit var binding : ActivityBankDetailsBinding
    lateinit var dialog :CustomProgressDialog
    var isBankUpdated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bank_details)
        setCustomActionBarListener()
        viewModel = ViewModelProvider(this, BankDetailsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(BankDetailsViewModel::class.java)
        dialog = CustomProgressDialog(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setClickListeners()
        observeViewModelData()
        getBankDetails()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.bank_details))
    }

    private fun getBankDetails(){
        isBankUpdated = StorePreferences(this).isBankUpdated == 1
        if(isBankUpdated){
            val userId :String =  StorePreferences(this).trainerId.toString()
            viewModel.getBankDetails(userId)
        }
    }

    private fun observeViewModelData(){
        viewModel.bankDetailsFetchResMUtLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.data?.let { list->
                        viewModel.bankDetails.postValue(list[0])
                        storeBankUpdateId(list[0].bankUpdateId)
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.bankDetailsUploadResMutLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    Toast.makeText(this,"Bank Details updated successfully", Toast.LENGTH_SHORT).show()
                    navigateToProfileOptionsScreen()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
        viewModel.errorDescription.observe(this, Observer {
            showToast(it)
        })
    }

    private fun navigateToProfileOptionsScreen(){
        finishAffinity()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(HomeActivity.showProfile,true)
        startActivity(intent)
    }

    private fun showToast(message : String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun storeBankUpdateId(banUpdateId: Int){
        StorePreferences(this).bankUpdateId = banUpdateId
    }

    private fun setClickListeners(){
        binding.ivEditProfile.setOnClickListener {
            viewModel.isBankDetailsEditable.value = true
        }
        binding.tvSave.setOnClickListener{
            uploadBankDetails()
        }
        binding.tvCancel.setOnClickListener{
            viewModel.isBankDetailsEditable.value = false
        }
    }

    private fun uploadBankDetails(){
        if(!viewModel.dataValid()){
            return
        }
        val userId = StorePreferences(this).trainerId
        val accountName = viewModel.bankDetails.value?.accountName!!
        val accountNumber = viewModel.bankDetails.value?.accountNumber!!
        val bankName = viewModel.bankDetails.value?.bankName!!
        val branchName = viewModel.bankDetails.value?.branchName!!
        val ifscCode = viewModel.bankDetails.value?.ifscCode!!

        val bankDataReq = BankDetailsUploadRequest(userId,accountName,accountNumber,bankName,branchName,ifscCode)
        if(isBankUpdated){
            val bankUpdateId = StorePreferences(this).bankUpdateId
            viewModel.updateBakDetails(bankUpdateId.toString(),bankDataReq)
        }else{
            viewModel.saveBankDetails(bankDataReq)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}