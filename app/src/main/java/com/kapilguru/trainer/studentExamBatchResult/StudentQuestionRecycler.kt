package com.kapilguru.trainer.studentExamBatchResult


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.StudentQuestionRecyclerBinding

class StudentQuestionRecycler(val questionClickListener: QuestionClickListener) : RecyclerView.Adapter<StudentQuestionRecycler.Holder>() {

    var questionPaperResponse = arrayListOf<QuestionPaperResponse>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var previousSelectedValue = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = StudentQuestionRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.position = position+1
        holder.view.model = questionPaperResponse[position]
        Log.d("TAG", "observeViewModel: ${questionPaperResponse[position]}")
    }

    override fun getItemCount(): Int = questionPaperResponse.size

    inner class Holder(itemView: StudentQuestionRecyclerBinding) : RecyclerView.ViewHolder(itemView.root) {
        var view = itemView
        var btn = itemView.positionNumber
        init {
            btn.setOnClickListener {
                if(!btn.isSelected) btn.isSelected = !btn.isSelected
//                if(previousSelectedValue!=-1) {
//                    questionPaperResponse[absoluteAdapterPosition]
////                    previousSelectedValue
//                }
                questionClickListener.onQuestionClicked(questionPaperResponse[absoluteAdapterPosition],position = absoluteAdapterPosition+1)
            }
        }
    }

}

interface QuestionClickListener {
     fun onQuestionClicked(questionPaperResponse: QuestionPaperResponse, position:Int)
}