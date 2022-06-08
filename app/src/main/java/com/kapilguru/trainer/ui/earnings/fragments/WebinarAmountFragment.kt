package com.kapilguru.trainer.ui.earnings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kapilguru.trainer.databinding.FragmentWebinarAmountBinding
import com.kapilguru.trainer.ui.earnings.adapter.EarningsDetailsWebinarAdapter
import com.kapilguru.trainer.ui.earnings.viewModel.EarningsDetailsViewModel

class WebinarAmountFragment : Fragment() {

    lateinit var webinarAmountBinding: FragmentWebinarAmountBinding
    val viewModel: EarningsDetailsViewModel by activityViewModels()
    lateinit var detailsAdapter: EarningsDetailsWebinarAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        webinarAmountBinding = FragmentWebinarAmountBinding.inflate(inflater)
        webinarAmountBinding.referralViewModel = viewModel
        detailsAdapter = EarningsDetailsWebinarAdapter()
        return webinarAmountBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webinarAmountBinding.webinarRecyclerView.adapter = detailsAdapter
        viewModel.earningsDetailsWebinar.observe(viewLifecycleOwner, {
            detailsAdapter.setDetailsWebinarList(it)
        })
    }
}