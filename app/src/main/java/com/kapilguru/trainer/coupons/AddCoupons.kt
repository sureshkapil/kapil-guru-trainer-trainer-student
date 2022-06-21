package com.kapilguru.trainer.coupons

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityAddCouponsBinding

class AddCoupons : BaseActivity() {

    lateinit var binding: ActivityAddCouponsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_coupons)
        setCustomActionBar()
    }


    private fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.actionbar, getString(R.string.coupons))
    }
}