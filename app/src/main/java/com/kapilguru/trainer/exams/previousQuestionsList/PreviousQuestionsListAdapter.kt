package com.kapilguru.trainer.exams.previousQuestionsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemPreviousQuestionCopyBinding
import com.kapilguru.trainer.exams.previousQuestionsList.model.PreviousQuestionData

class PreviousQuestionsListAdapter : RecyclerView.Adapter<PreviousQuestionsListAdapter.PreviousQuestionViewHolder>() {
    private var mPreviousQuestionsList = ArrayList<PreviousQuestionData>()
    private var mLastSelectedPosition: Int = -1

    fun setData(previousQuestions: ArrayList<PreviousQuestionData>) {
        mPreviousQuestionsList = previousQuestions
        notifyDataSetChanged()
    }

    fun getSelectedQuestions(): ArrayList<PreviousQuestionData> {
        val selectedQuestion = mPreviousQuestionsList.filter {
            it.isSelected
        }
        return selectedQuestion as ArrayList<PreviousQuestionData>
    }

    inner class PreviousQuestionViewHolder(val binding: ItemPreviousQuestionCopyBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            setOptionsVisibility()
        }

        private fun setOptionsVisibility(){
            binding.clQuestion.setOnClickListener {
                if (mLastSelectedPosition == -1) {
                    mPreviousQuestionsList[bindingAdapterPosition].shouldShowOptions = true
                    mLastSelectedPosition = bindingAdapterPosition
                    notifyItemChanged(bindingAdapterPosition)
                } else if (mLastSelectedPosition == bindingAdapterPosition) {
                    mPreviousQuestionsList[bindingAdapterPosition].shouldShowOptions = !mPreviousQuestionsList[bindingAdapterPosition].shouldShowOptions
                    mLastSelectedPosition = bindingAdapterPosition
                    notifyItemChanged(bindingAdapterPosition)
                } else {
                    mPreviousQuestionsList[mLastSelectedPosition].shouldShowOptions = false
                    notifyItemChanged(mLastSelectedPosition)
                    mLastSelectedPosition = bindingAdapterPosition
                    mPreviousQuestionsList[mLastSelectedPosition].shouldShowOptions = true
                    notifyItemChanged(mLastSelectedPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousQuestionViewHolder {
        val binding = ItemPreviousQuestionCopyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PreviousQuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PreviousQuestionViewHolder, position: Int) {
        holder.binding.model = mPreviousQuestionsList[position]
        holder.binding.actvQuestionNo.text = (position + 1).toString() + ")"
        holder.binding.cbSelect.setOnCheckedChangeListener { buttonView, isChecked ->
            mPreviousQuestionsList[position].isSelected = isChecked
        }
    }

    override fun getItemCount(): Int {
        return mPreviousQuestionsList.size
    }
}