package com.kapilguru.trainer.ui.earnings.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentCourseAmountBinding
import com.kapilguru.trainer.ui.earnings.adapter.EarningsDetailsCourseAdapter
import com.kapilguru.trainer.ui.earnings.viewModel.EarningsDetailsViewModel


class CourseAmountFragment : Fragment() {

    lateinit var earningsViewBinding:FragmentCourseAmountBinding
    val viewModel:EarningsDetailsViewModel  by activityViewModels()
    lateinit var detailsAdapter:EarningsDetailsCourseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        earningsViewBinding=FragmentCourseAmountBinding.inflate(inflater)

        earningsViewBinding.earningsDetailsViewModel=viewModel

        earningsViewBinding.lifecycleOwner = this.requireActivity()

        return earningsViewBinding.root
       // return inflater.inflate(R.layout.fragment_course_amount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsAdapter = EarningsDetailsCourseAdapter()


        viewModel.earningsDetailsData.observe(viewLifecycleOwner, {
            detailsAdapter.setDetailsCourseList(it)
        })
        earningsViewBinding.earningsRecyclerView.adapter=detailsAdapter
    }


}