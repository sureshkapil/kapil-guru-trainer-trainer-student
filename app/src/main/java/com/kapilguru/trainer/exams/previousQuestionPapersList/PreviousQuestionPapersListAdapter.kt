package com.kapilguru.trainer.exams.previousQuestionPapersList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ItemPreviousQuestionPaperBinding
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.PreviousQuestionPapersListData

class PreviousQuestionPapersListAdapter(val mListener: QuestionPaperClickListener) : RecyclerView.Adapter<PreviousQuestionPapersListAdapter.PreviousQuestionPapersItemViewHolder>() {
    private val mPreviousQuestionPapersList = ArrayList<PreviousQuestionPapersListData>()

    fun setData(previousQuestionPapersList: ArrayList<PreviousQuestionPapersListData>) {
        mPreviousQuestionPapersList.addAll(previousQuestionPapersList)
        notifyDataSetChanged()
    }

    inner class PreviousQuestionPapersItemViewHolder(val binding: ItemPreviousQuestionPaperBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                mListener.onQuestionPaperClicked(mPreviousQuestionPapersList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousQuestionPapersItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemPreviousQuestionPaperBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_previous_question_paper, parent, false)
        return PreviousQuestionPapersItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PreviousQuestionPapersItemViewHolder, position: Int) {
        holder.binding.model = mPreviousQuestionPapersList[position]
    }

    override fun getItemCount(): Int {
        return mPreviousQuestionPapersList.size
    }

    interface QuestionPaperClickListener {
        fun onQuestionPaperClicked(previousQuestionPaper: PreviousQuestionPapersListData)
    }
}