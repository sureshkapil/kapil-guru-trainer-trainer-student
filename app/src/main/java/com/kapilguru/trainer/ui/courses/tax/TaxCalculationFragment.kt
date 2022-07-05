package com.kapilguru.trainer.ui.courses.tax

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomDrawableClickListener
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentTaxCalculationBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.showAppToast
import kotlinx.android.synthetic.main.fragment_add_course_titile_and_description.*
import kotlinx.android.synthetic.main.fragment_add_course_titile_and_description.aCETCourseOfferPriceValue
import kotlinx.android.synthetic.main.fragment_add_course_titile_and_description.aCETCoursePriceValue
import kotlinx.android.synthetic.main.fragment_tax_calculation.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaxCalculationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaxCalculationFragment : Fragment() {

    private var param1: PriceModel? = null
    lateinit var viewModel: TaxCalculationFragmentViewModel
    lateinit var viewBinding: FragmentTaxCalculationBinding
    lateinit var dialog: CustomProgressDialog
    private val TAG = "TaxCalculationFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable<PriceModel>(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTaxCalculationBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, TaxCalculationFragmentViewModelFactory(
                ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),))
            .get(TaxCalculationFragmentViewModel::class.java)
        viewBinding.model = viewModel
        viewBinding.lifecycleOwner = this
        dialog = CustomProgressDialog(this.requireActivity())
        param1?.let {
            viewModel.actualFee.value = it.actualFee
            viewModel.discountAmount.value = it.discountAmount
            viewModel.fee.value = it.fee
            viewModel.isTaxChargesAdded.value = it.isTaxChargesAdded
            viewModel.isInternetChargesAdded.value = it.internetCharges!! > 0.0
        }?: kotlin.run {

        }
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTax()
        observeViewModel()
        onclickListerners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onclickListerners() {
        viewBinding.aCETCoursePriceValue.setOnTouchListener(
            CustomDrawableClickListener( textInputEditText =aCETCoursePriceValue,
            title = "What is the Course price?",
            subTitle = "(Price of the course)",
            lifecycleOwner = this)
        )

        viewBinding.aCETCourseOfferPriceValue.setOnTouchListener(
            CustomDrawableClickListener( textInputEditText =aCETCourseOfferPriceValue,
            title = "What is the offered price ?",
            subTitle = "(Discounted Price)",
            lifecycleOwner = this)
        )

        viewBinding.aCTVCourseFinalPriceValue.setOnTouchListener(
            CustomDrawableClickListener( textInputEditText =aCTVCourseFinalPriceValue,
            title = "What is the Effective price ?",
            subTitle = "(Price shown to the Student)",
            lifecycleOwner = this)
        )
    }

    private fun observeViewModel() {
        viewModel.taxCalculationResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.data?.let { response ->
                        if (response.isNotEmpty()) {
                            viewModel.taxCalculationResponseApi.value = response.filter { item -> item.id == 1 }[0]
                            callaginOnEditToUpdateInfo()
                        }
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }

            }
        })

        viewModel.fee.observe(viewLifecycleOwner, Observer {
            if(it.trim().isNotEmpty()) viewModel.calculateFinalPrice()
        })

        viewModel.discountAmount.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.trim().isNotEmpty()) viewModel.calculateFinalPrice()

            }
        })

        viewModel.isInternetChargesAdded.observe(viewLifecycleOwner, Observer {
            viewModel.calculateFinalPrice()
        })

        viewModel.errorText.observe(viewLifecycleOwner, Observer {
            showerorText(it)
        })

    }

    private fun callaginOnEditToUpdateInfo() {
        param1?.let {
            viewModel.calculateFinalPrice()
        }

    }

    private fun showerorText(it: String?) {
        showAppToast(this.requireContext(),it!!)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaxCalculationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(priceModel: PriceModel?) =
            TaxCalculationFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PARAM1, priceModel)
//                putString(ARG_PARAM2, param2)
            }
        }
    }


    fun getPriceData(): PriceModel?  {
        return viewModel.priceModel.value?.apply {
            fee = viewModel.fee.value
            discountAmount = viewModel.discountAmount.value
            actualFee = viewModel.actualFee.value
            isInternetChargesAdded = viewModel.isInternetChargesAdded.value!!
            isTaxChargesAdded = viewModel.isTaxChargesAdded.value!!
            internetCharges = if (viewModel.isInternetChargesAdded.value!!) {
                 viewModel.taxCalculationResponseApi.value!!.addPercent
            }  else {
                 0.0
            }
        }
    }
}