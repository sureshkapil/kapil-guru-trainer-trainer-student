package com.kapilguru.trainer.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.SESSION_OUT
import com.kapilguru.trainer.allSubscription.AllSubscriptionActivity
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.onBoarding.OnBoardingActivity
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.home.HomeActivity

class SplashScreenActivity : AppCompatActivity() {


    lateinit var splashScreenViewModel: SplashScreenViewModel
    lateinit var dialog: CustomProgressDialog
    private val TAG = "SplashScreen"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setUpViewModel()
        verifyAccessToken()
        viewModelObserver()
    }

    private fun viewModelObserver() {
        splashScreenViewModel.profileDataResponse.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.let {
                        if (response.data.status == 200) {
                            navigateToInitialScreen()
                        } else {
                            clearSharedPreferences()
                        }
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    if (response.code == SESSION_OUT) {
                        clearSharedPreferences()
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }
        })
    }

    private fun clearSharedPreferences() {
        val pref = StorePreferences(this)
        pref.clearStorePreferences()
        navigateToInitialScreen()
    }

    private fun navigateToInitialScreen() {
        val pref = StorePreferences(this)
        if (pref.token!!.isNullOrEmpty()) {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        } else {
            if (StorePreferences(this).isSubscribed == 1) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                launchSubscriptionsActivity()
            }
        }
    }

    private fun launchSubscriptionsActivity() {
        startActivity(Intent(this, AllSubscriptionActivity::class.java))
        finish()
    }

    private fun verifyAccessToken() {
        val pref = StorePreferences(this)
        if (pref.userId != 0) {
            splashScreenViewModel.getProfileData(pref.userId.toString())
        } else {
            navigateToInitialScreen()
        }
    }

    private fun setUpViewModel() {
        splashScreenViewModel = ViewModelProvider(
            this, SplashScreenViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        ).get(SplashScreenViewModel::class.java)
        dialog = CustomProgressDialog(this)
    }


    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok) { dialog, id ->
                    setCancelable(true)
                }
                setMessage(message)
            }
            builder.create()
        }
        alertDialog.show()
    }

}