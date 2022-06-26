package com.kapilguru.trainer.student.myClassroom.totalActiveBatches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemStudentActiveCourseBinding
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentActiveBatchData

/*list is stored in Linked Hash Map where the key is course id*/
class StudentActiveCoursesAdapter(val mListener: StudentCourseClickListerner) : RecyclerView.Adapter<StudentActiveCoursesAdapter.StudentActiveBatchViewHolder>() {
    private val TAG = "ActiveCoursesAdapter"
    private var mBatchList = LinkedHashMap<Int, ArrayList<StudentActiveBatchData>>()
    private var mKeyList = ArrayList<Int>()

    fun setData(batchList: LinkedHashMap<Int, ArrayList<StudentActiveBatchData>>?) {
        batchList?.let {
            mBatchList = batchList
            mKeyList = ArrayList<Int>(mBatchList.keys)
            notifyDataSetChanged()
        }
    }

    class StudentActiveBatchViewHolder(val binding: ItemStudentActiveCourseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentActiveBatchViewHolder {
        val binding = ItemStudentActiveCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentActiveBatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentActiveBatchViewHolder, position: Int) {
        holder.binding.batch = mBatchList.get(mKeyList[position])?.get(0)
        holder.binding.root.setOnClickListener {
            mListener.onCourseClicked(mBatchList[getKey(position)]!!)
        }
    }

    override fun getItemCount(): Int {
        return mBatchList.size
    }

    /*return the key based on the index.*/
    private fun getKey(position: Int): Int {
        return mKeyList[position]
    }

    interface StudentCourseClickListerner {
        fun onCourseClicked(batchList: ArrayList<StudentActiveBatchData>)
    }
}