package com.kapilguru.trainer.feeManagement.feeFollowUps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentTodayFeeFollowUpBinding
import com.kapilguru.trainer.feeManagement.FeeManagementViewModel
import com.kapilguru.trainer.network.Status

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpComingFeeFollowUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpComingFeeFollowUpFragment : Fragment() {

    lateinit var binding: FragmentTodayFeeFollowUpBinding
    val viewModel: FeeManagementViewModel by viewModels({ requireActivity() })
    lateinit var adapter: FeeFollowUpsRecyclerAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTodayFeeFollowUpBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(this.requireContext())
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModelObserver()
    }

    private fun setUpRecyclerView() {
        adapter = FeeFollowUpsRecyclerAdapter()
        binding.recy.adapter = adapter
    }

    private fun viewModelObserver() {

        viewModel.upComingFeeFollowUpResponse.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) addDataToAdapter(it)
        })
    }

    private fun addDataToAdapter(data: List<FeeFollowUpResponseApi>) {
        adapter.listItem = data as ArrayList<FeeFollowUpResponseApi>
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpComingFeeFollowUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = UpComingFeeFollowUpFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}