package com.kapilguru.trainer.student.homeActivity.liveCourses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemLiveCourseBinding
import com.kapilguru.trainer.databinding.ItemLiveCourseLargeBinding
import com.kapilguru.trainer.student.homeActivity.liveCourses.model.LiveCourseResData

class LiveCourseAdapter(val mListener: ClickListener, val showCardWithMaxWidth: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "LiveCourseAdapter"
    private var mLiveCourseList = ArrayList<LiveCourseResData>()

    fun setLiveCourseList(liveCourseList: ArrayList<LiveCourseResData>) {
        mLiveCourseList = liveCourseList
        notifyDataSetChanged()
    }

    inner class LiveCourseViewHolder(val binding: ItemLiveCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnKnowMore.setOnClickListener {
                mListener.onViewMoreClicked(mLiveCourseList[bindingAdapterPosition])
            }
        }
    }

    inner class LiveCourseViewHolderLarge(val binding: ItemLiveCourseLargeBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnKnowMore.setOnClickListener {
                mListener.onViewMoreClicked(mLiveCourseList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == CARDTYPE.SMALL.type){
            val binding = ItemLiveCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LiveCourseViewHolder(binding)
        }else{
            val binding = ItemLiveCourseLargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LiveCourseViewHolderLarge(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == CARDTYPE.SMALL.type) {
            (holder as LiveCourseViewHolder).binding.model = mLiveCourseList[position]
        } else {
            (holder as LiveCourseViewHolderLarge).binding.model = mLiveCourseList[position]
        }
    }

    override fun getItemCount(): Int {
        return mLiveCourseList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (showCardWithMaxWidth) {
            CARDTYPE.LARGE.type
        } else {
            CARDTYPE.SMALL.type
        }
    }

    interface ClickListener {
        fun onViewMoreClicked(liveCourse: LiveCourseResData)
    }

    enum class CARDTYPE(val type: Int) {
        SMALL(0), LARGE(1)
    }
}