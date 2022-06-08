package com.kapilguru.trainer.myClassroom.totalActiveBatches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.databinding.ItemActiveCourseBinding
import java.util.LinkedHashMap

/*list is stored in Linked Hash Map where the key is course id*/
class ActiveCoursesAdapter(val mListener : CourseClickListerner) : RecyclerView.Adapter<ActiveCoursesAdapter.ActiveBatchViewHolder>() {
    private val TAG = "ActiveCoursesAdapter"
    private var mBatchList = LinkedHashMap<Int, ArrayList<NewMessageData>>()
    private var mKeyList = ArrayList<Int>()

    fun setData(batchList: LinkedHashMap<Int, ArrayList<NewMessageData>>?) {
        batchList?.let {
            mBatchList = batchList
            mKeyList = ArrayList<Int>(mBatchList.keys)
            notifyDataSetChanged()
        }
    }

    class ActiveBatchViewHolder(val binding: ItemActiveCourseBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveBatchViewHolder {
        val binding = ItemActiveCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActiveBatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActiveBatchViewHolder, position: Int) {
        holder.binding.batch = mBatchList.get(mKeyList[position])?.get(0)
        holder.binding.root.setOnClickListener {
            mListener.onCourseClicked(mBatchList[getKey(position)]!!)
        }
    }

    override fun getItemCount(): Int {
        return mBatchList.size
    }

    /*return the key based on the index.*/
    private fun getKey(position : Int) : Int{
        return mKeyList[position]
    }

    interface CourseClickListerner{
        fun onCourseClicked(batchList : ArrayList<NewMessageData>)
    }
}