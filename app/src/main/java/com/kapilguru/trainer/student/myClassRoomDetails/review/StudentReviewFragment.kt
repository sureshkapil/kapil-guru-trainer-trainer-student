package com.kapilguru.trainer.student.myClassRoomDetails.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentReviewBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.showAppToast
import com.kapilguru.trainer.student.myClassRoomDetails.viewModel.StudentMyClassDetailsViewModel
import kotlinx.android.synthetic.main.custom_key_value_view.view.*


class StudentReviewFragment : Fragment() {
    lateinit var progressDialog: CustomProgressDialog
    private val viewModels: StudentMyClassDetailsViewModel by activityViewModels()
    lateinit var binding: FragmentReviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        binding.lifecycleOwner = this
        binding.review = viewModels.reviewStudentRequest.value
        bindData()
        onClickListener()
        observerLiveData()
    }

    private fun observerLiveData() {
        viewModels.reviewStudentResponse.observe(this.viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    clearData()
                    showAppToast(requireContext(), "Review Posted Successfully")
                    progressDialog.dismissLoadingDialog()
                }
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }

            }
        }
    }

    private fun onClickListener() {
        binding.postBtn.setOnClickListener {
            binding.review?.let {
                when {
                    it.studentRating == 0f -> showAppToast(requireContext(), "Provide rating")
                    it.studentRatingText.isEmpty() -> showAppToast(requireContext(), "Provide feedback")
                    else -> viewModels.updateReview(it)
                }
            }
        }

        binding.resetBtn.setOnClickListener { clearData() }
    }

    private fun clearData() {
        binding.review?.let {
            it.studentRating = 0f
            it.studentRatingText = ""
            binding.review = it
        }
    }

    private fun bindData() {
        val pref = StorePreferences(requireContext())
        val data = viewModels.batchApiResponse.value?.data?.studentBatchDetailsData?.get(0)
        data?.let {
            val reviewRequest = viewModels.reviewStudentRequest.value
            reviewRequest?.studentId = pref.userId.toString()
            reviewRequest?.batchId = data.batchId

            viewModels.reviewStudentRequest.postValue(reviewRequest)
            binding.review = reviewRequest

            binding.keyValuePare.text_key.text = "Trainer"
            binding.keyValuePare.text_value.text = data.trainerName
            binding.studentNameTv.text = pref.userName
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() = StudentReviewFragment()
    }
}