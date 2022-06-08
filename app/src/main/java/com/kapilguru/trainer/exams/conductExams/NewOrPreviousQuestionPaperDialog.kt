package com.kapilguru.trainer.exams.conductExams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kapilguru.trainer.databinding.DialogNewOrPreviousQuestionPaperBinding

class NewOrPreviousQuestionPaperDialog(val mListener: QuestionPaperTypeListener) : DialogFragment() {
    val TAG = "NewOrPreviousQuestionPaperDialog"
    lateinit var binding: DialogNewOrPreviousQuestionPaperBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogNewOrPreviousQuestionPaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
        getDialog()?.setCanceledOnTouchOutside(false);
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.3).toInt()
        dialog!!.window?.setLayout(width, height)
    }

    private fun setClickListeners() {
        binding.actvNewQuestionPaper.setOnClickListener {
            mListener.onNewQuestionPaperClicked()
        }

        binding.actvPreviousQuestionPaper.setOnClickListener {
            mListener.onPreviousQuestionPaperClicked()
        }
        binding.actvCross.setOnClickListener {
            mListener.onCloseClicked()
        }
    }

    interface QuestionPaperTypeListener {
        fun onNewQuestionPaperClicked()
        fun onPreviousQuestionPaperClicked()
        fun onCloseClicked()
    }

    companion object {
        val TAG = "NewOrPreviousQuestionPaperDialog"
    }
}

