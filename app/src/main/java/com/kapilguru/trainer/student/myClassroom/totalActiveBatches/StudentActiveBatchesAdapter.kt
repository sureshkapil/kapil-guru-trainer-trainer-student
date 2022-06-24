package com.kapilguru.trainer.student.myClassroom.totalActiveBatches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemStudentActiveBatchesBinding
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentActiveBatchData

class StudentActiveBatchesAdapter(val mListener: StudentBatchClickListener) : RecyclerView.Adapter<StudentActiveBatchesAdapter.BatchViewHolder>() {
    private var mBatchList = ArrayList<StudentActiveBatchData>()

    fun setData(batchList: ArrayList<StudentActiveBatchData>) {
        mBatchList = batchList
        notifyDataSetChanged()
    }

    inner class BatchViewHolder(val binding: ItemStudentActiveBatchesBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.actvOverview.setOnClickListener {
                mListener.onOverViewClicked(mBatchList[bindingAdapterPosition])
            }
            binding.studentDetails.setOnClickListener {
                mListener.onOverViewClicked(mBatchList[bindingAdapterPosition])
            }

            binding.activeRecordings.setOnClickListener {
//                mListener.onRecordingsClicked(mLiveUpComingClassList[bindingAdapterPosition])
            }

            binding.activeStudyMaterial.setOnClickListener {
                mListener.onStudyMaterialClicked(mBatchList[bindingAdapterPosition])
            }

            binding.activeExam.setOnClickListener {
                mListener.onExamClicked(mBatchList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val binding = ItemStudentActiveBatchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.binding.model = mBatchList[position]
        holder.binding.root.setOnClickListener {
            mListener.onBatchClicked(mBatchList[position])
        }
    }

    override fun getItemCount(): Int {
        return mBatchList.size
    }

    interface StudentBatchClickListener {
        fun onBatchClicked(batchData: StudentActiveBatchData)
        fun onOverViewClicked(batchData: StudentActiveBatchData)
        fun onStudyMaterialClicked(batchData: StudentActiveBatchData)
        fun onExamClicked(batchData: StudentActiveBatchData)
        fun onCompletionRequestClicked(batchData: StudentActiveBatchData)
    }
}