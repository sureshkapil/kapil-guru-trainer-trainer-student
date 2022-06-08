package com.kapilguru.trainer.myClassRoomDetails.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityOverViewBinding
import com.kapilguru.trainer.databinding.FragmentExamBinding
import com.kapilguru.trainer.exams.ExamsActivity
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.viewModel.BatchCompletionReqViewModel

class ExamFragment : Fragment() {
    private val TAG = "ExamFragment"
    val viewModel: BatchCompletionReqViewModel by activityViewModels()
    lateinit var binding: FragmentExamBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentExamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    private fun setClickListeners(){
        binding.btnConductExam.setOnClickListener {
            viewModel.courseId.value?.let { courseId ->
                ExamsActivity.launchActivity(requireActivity(),courseId.toInt())
            }
        }
    }
}