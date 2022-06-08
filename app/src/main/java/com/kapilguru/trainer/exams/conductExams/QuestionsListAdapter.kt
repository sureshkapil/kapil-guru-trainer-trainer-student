package com.kapilguru.trainer.exams.conductExams

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemPreviousQuestionEditBinding
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.Question

class QuestionsListAdapter(val mListener: QuestionClickListener) : RecyclerView.Adapter<QuestionsListAdapter.QuestionViewHolder>() {
    private val TAG = "PreviousQuestionsListAdapter"
    private var mQuestionsList = ArrayList<Question>()
    private var mLastSelectedPosition: Int = -1

    fun setData(previousQuestions: ArrayList<Question>?) {
        previousQuestions?.let {
            mQuestionsList = previousQuestions
            notifyDataSetChanged()
        } ?: run {
            Log.d(TAG, "setData: empty list")
        }
    }

    inner class QuestionViewHolder(val binding: ItemPreviousQuestionEditBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.acivDelete.setOnClickListener {
                mListener.onDeleteClicked(mQuestionsList[bindingAdapterPosition])
            }
            binding.acivEdit.setOnClickListener {
                mListener.onEditClicked(mQuestionsList[bindingAdapterPosition])
            }
            setOptionsVisibility()
        }

        private fun setOptionsVisibility(){
            binding.clQuestion.setOnClickListener {
                if (mLastSelectedPosition == -1) {
                    mQuestionsList[bindingAdapterPosition].shouldShowOptions = true
                    mLastSelectedPosition = bindingAdapterPosition
                    notifyItemChanged(bindingAdapterPosition)
                } else if (mLastSelectedPosition == bindingAdapterPosition) {
                    mQuestionsList[bindingAdapterPosition].shouldShowOptions = !mQuestionsList[bindingAdapterPosition].shouldShowOptions
                    mLastSelectedPosition = bindingAdapterPosition
                    notifyItemChanged(bindingAdapterPosition)
                } else {
                    mQuestionsList[mLastSelectedPosition].shouldShowOptions = false
                    notifyItemChanged(mLastSelectedPosition)
                    mLastSelectedPosition = bindingAdapterPosition
                    mQuestionsList[mLastSelectedPosition].shouldShowOptions = true
                    notifyItemChanged(mLastSelectedPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemPreviousQuestionEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.binding.actvQuestionNo.text = (position + 1).toString() + ")"
        holder.binding.model = mQuestionsList[position]

    }

    override fun getItemCount(): Int {
        return mQuestionsList.size
    }

    interface QuestionClickListener {
        fun onEditClicked(question: Question)
        fun onDeleteClicked(question: Question)
    }
}