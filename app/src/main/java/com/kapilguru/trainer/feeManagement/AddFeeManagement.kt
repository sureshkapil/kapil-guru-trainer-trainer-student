package com.kapilguru.trainer.feeManagement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityAddFeeManagementBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import kotlinx.android.synthetic.main.add_syllabus.view.*

class AddFeeManagement : BaseActivity() {
    lateinit var binding: ActivityAddFeeManagementBinding
    lateinit var viewModel: FeeManagementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fee_management)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_fee_management)
        viewModel = ViewModelProvider(this, FeeManagementViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(FeeManagementViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setCustomActionBarListener()
        setClickListeners()
    }


    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.add_fee_management))
    }

    private fun setClickListeners() {
        binding.addInstalment.setOnClickListener {
            validateAndCalculate()
        }
    }

    private fun validateAndCalculate() {
        if (viewModel.dueAmount.value!! > 0 && viewModel.numberOfInstallments.value!! > 0) {
            setInstalmentLayout(calculateSplitAmount(viewModel.dueAmount.value, viewModel.numberOfInstallments.value), viewModel.numberOfInstallments.value!!)
            navigateToBottom()
        }
    }

    private fun navigateToBottom() {
        binding.scrollView.post {
            binding.scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun calculateSplitAmount(dueAmount: Double?, numberOfInstallments:Int?):Double {
        return dueAmount!!/numberOfInstallments!!
    }

    private fun setInstalmentLayout(calculateSplitAmount: Double, numberOfInstallments: Int) {
        for(i in 0 until numberOfInstallments) {
            val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v: View = layoutInflater.inflate(R.layout.add_instalment, null)
            v.imageView.setOnClickListener {
                binding.lLayoutParent.removeView(it.parent as View)
            }
//            v.imageView.setOnClickListener {
//                binding.lLayoutParent.removeView(it.parent as View)
//            }
            binding.lLayoutParent.addView(v)
            val params = v.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, 3, 0, 3)
            v.layoutParams = params
        }
    }

}