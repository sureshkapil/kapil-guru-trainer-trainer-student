package com.kapilguru.trainer.exams.conductExams

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kapilguru.trainer.databinding.FragmentQuestionPaperTitleBinding
import com.kapilguru.trainer.showAppToast
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class QuestionPaperTitleDialogFragment : DialogFragment() {
    lateinit var binding: FragmentQuestionPaperTitleBinding
    private var mListener: QuestionPaperTitleListener? = null
    lateinit var course : CourseResponse

    fun setQuestionPaperTitleListener(questionPaperTitleListener: QuestionPaperTitleListener) {
        mListener = questionPaperTitleListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentQuestionPaperTitleBinding.inflate(inflater, container, false)
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        course = arguments?.getParcelable(COURSE_KEY)!!
        binding.course = course
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.actvCross.setOnClickListener {
            closeDialog()
        }
        binding.btnSave.setOnClickListener {
            val questionPaperTitle = binding.tietQuestionPaperTitle.text.toString()
            if (TextUtils.isEmpty(questionPaperTitle)) {
                showAppToast(requireContext(), "Please Enter Question Paper Title")
            } else {
                mListener?.onSaveQuestionPaperTitleClicked(questionPaperTitle,course)
            }
        }
    }

    fun closeDialog() {
        mListener = null
        dismiss()
    }

    companion object {
        val COURSE_KEY = "COURSE"

        @JvmStatic
        fun newInstance(course: CourseResponse) = QuestionPaperTitleDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(COURSE_KEY, course)
            }
        }
    }

    interface QuestionPaperTitleListener {
        fun onSaveQuestionPaperTitleClicked(questionPaperTitle: String, course: CourseResponse)
    }
}