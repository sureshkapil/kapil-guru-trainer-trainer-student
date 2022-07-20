package com.kapilguru.trainer.student.homeActivity.liveCourses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemLiveCourseBinding
import com.kapilguru.trainer.student.homeActivity.liveCourses.model.LiveCourseResData

class LiveCourseAdapter(val mListener : ClickListener) : RecyclerView.Adapter<LiveCourseAdapter.LiveCourseViewHolder>() {
    private val TAG = "LiveCourseAdapter"
    private var mLiveCourseList = ArrayList<LiveCourseResData>()

    fun setLiveCourseList(liveCourseList: ArrayList<LiveCourseResData>) {
        mLiveCourseList = liveCourseList
        notifyDataSetChanged()
    }

    inner class LiveCourseViewHolder(val binding: ItemLiveCourseBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.btnKnowMore.setOnClickListener {
                mListener.onViewMoreClicked(mLiveCourseList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveCourseViewHolder {
        val binding = ItemLiveCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LiveCourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LiveCourseViewHolder, position: Int) {
        holder.binding.model = mLiveCourseList[position]
    }

    override fun getItemCount(): Int {
        return mLiveCourseList.size
    }
    interface ClickListener{
        fun onViewMoreClicked(liveCourse : LiveCourseResData)
    }
}