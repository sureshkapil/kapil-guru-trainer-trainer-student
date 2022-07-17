package com.kapilguru.trainer.certificate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityAddCouponsBinding
import com.kapilguru.trainer.databinding.ActivityCertificateBinding

class CertificateActivity : BaseActivity() {

    lateinit var binding: ActivityCertificateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_certificate)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_certificate)
        setCustomActionBar()
    }

    private fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.actionbar, getString(R.string.certificate))
    }


}