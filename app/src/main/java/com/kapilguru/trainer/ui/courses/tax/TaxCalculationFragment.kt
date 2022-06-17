package com.kapilguru.trainer.ui.courses.tax

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentTaxCalculationBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaxCalculationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaxCalculationFragment : Fragment() {

    private var param1: PriceModel? = null
    private var param2: String? = null
    lateinit var viewModel: TaxCalculationFragmentViewModel
    lateinit var viewBinding: FragmentTaxCalculationBinding
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable<PriceModel>(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTaxCalculationBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, TaxCalculationFragmentViewModelFactory(
                ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),))
            .get(TaxCalculationFragmentViewModel::class.java)
        viewBinding.lifecycleOwner = this.requireActivity()
        dialog = CustomProgressDialog(requireContext())
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTax()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.taxCalculationResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.let {

                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }

            }
        })
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
        fun newInstance() =
            TaxCalculationFragment().apply {
            /*arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }*/
        }
    }

    fun getPriceData(): PriceModel? =  viewModel.priceModel.value

}