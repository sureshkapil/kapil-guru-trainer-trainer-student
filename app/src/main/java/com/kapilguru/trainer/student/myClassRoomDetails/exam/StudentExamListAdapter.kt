package com.kapilguru.trainer.student.myClassRoomDetails.exam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemStudentExamListBinding
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListItemResData

class StudentExamListAdapter(val mListener: ExamItemClickListener) : RecyclerView.Adapter<StudentExamListAdapter.ViewHolder>() {
    private var mExamList = ArrayList<StudentQuestionPaperListItemResData>()
    private var mBatchCode = ""
    fun setData(batchCode: String?, examList: ArrayList<StudentQuestionPaperListItemResData>) {
        mExamList = examList
//        mBatchCode = "Batch Code - "+batchCode
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemStudentExamListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val questionPaper = mExamList[bindingAdapterPosition]
                if (questionPaper.isCompleted == 1) {
                    mListener.onViewResultClicked(questionPaper)
                } else {
                    mListener.onStartExamClicked(questionPaper)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentExamListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mExamList[position]
//        holder.binding.actvBatchCode.text = mBatchCode
    }

    override fun getItemCount(): Int {
        return mExamList.size
    }

    interface ExamItemClickListener {
        fun onStartExamClicked(questionPaper: StudentQuestionPaperListItemResData)
        fun onViewResultClicked(questionPaper: StudentQuestionPaperListItemResData)
    }
}