package com.kapilguru.trainer.onBoarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityOnBoardingBinding
import com.kapilguru.trainer.login.LoginActivity
import com.kapilguru.trainer.ui.earnings.EarningsActivity
import kotlinx.android.synthetic.main.activity_on_boarding.*
import java.util.*


class OnBoardingActivity : AppCompatActivity() {
    private val TAG = "OnBoarding"
    private var onBoardingAdapter: OnBoardingAdapter? = null
    lateinit var activityOnBoardingBinding: ActivityOnBoardingBinding
    lateinit var onBoardingViewModel: OnBoardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOnBoardingBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        onBoardingViewModel = ViewModelProvider(this).get(OnBoardingViewModel(application)::class.java)
        activityOnBoardingBinding.viewModel = onBoardingViewModel
        activityOnBoardingBinding.lifecycleOwner = this
        activityOnBoardingBinding.clickHandler = this
        setAdapter()
        viewModelObserver()
        setCurrentOnboardingIndicators(0)
        registerOnPageChangeCallBack()
    }

    private fun setAdapter() {
        onBoardingAdapter = OnBoardingAdapter()
        onboardingViewPager.adapter = onBoardingAdapter
    }

    private fun viewModelObserver() {
        onBoardingViewModel.setOnBoardingItem()
        onBoardingViewModel.listOfOnBoardingItem.observe(this, Observer {
            onBoardingAdapter?.setData(it as ArrayList<OnBoardingItem>)
            setOnboadingIndicator()
        })
    }

    private fun registerOnPageChangeCallBack() {
        onboardingViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicators(position)
            }
        })
    }

    fun onLoginClick(v: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun onRegisterClick(v: View) {
        startActivity(Intent(this, EarningsActivity::class.java))
    }

    private fun setOnboadingIndicator() {
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in 0..onBoardingAdapter?.itemCount!! - 1) {
            val indicators = ImageView(applicationContext)

            indicators.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, R.drawable.onboarding_indicator_active_two
                )
            )
            indicators.layoutParams = layoutParams
            activityOnBoardingBinding.layoutOnboardingIndicators.addView(indicators)
        }
    }

    private fun setCurrentOnboardingIndicators(index: Int) {
        val childCount = activityOnBoardingBinding.layoutOnboardingIndicators.childCount
        for (i in 0..childCount) {
            val imageView = activityOnBoardingBinding.layoutOnboardingIndicators.getChildAt(i)?.let {
                it as ImageView
            }
            if (i == index) {
                imageView?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_active
                    )
                )
            } else {
                imageView?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_active_two
                    )
                )
            }
        }
    }
}
