package com.kapilguru.trainer.ui.earnings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.CourseEarningItemViewBinding
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsCourse

class EarningsDetailsCourseAdapter() : RecyclerView.Adapter<EarningsDetailsCourseAdapter.EarningsDetailsViewHolder>() {

    var earningsDetailsCourseList = listOf<EarningsDetailsCourse>()
    var mLastSelectedPosition = -1

    fun setDetailsCourseList(earningsDetailsCourseList: ArrayList<EarningsDetailsCourse>) {
        this.earningsDetailsCourseList=earningsDetailsCourseList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarningsDetailsViewHolder {
      val inflater= LayoutInflater.from(parent.context)
        val binding=CourseEarningItemViewBinding.inflate(inflater,parent,false)
        return EarningsDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EarningsDetailsViewHolder, position: Int) {
              holder.view.earningsData=earningsDetailsCourseList[position]
              holder.parentCard.setOnClickListener {
                  when (mLastSelectedPosition) {
                      -1 -> {
                          earningsDetailsCourseList[position].shouldShowChild = true
                          mLastSelectedPosition = position
                          notifyItemChanged(position)
                      }
                      position -> {
                          earningsDetailsCourseList[position].shouldShowChild = !earningsDetailsCourseList[position].shouldShowChild
                          notifyItemChanged(position)
                      }
                      else -> {
                          earningsDetailsCourseList[mLastSelectedPosition].shouldShowChild = false
                          earningsDetailsCourseList[position].shouldShowChild = true
                          notifyItemChanged(position)
                          notifyItemChanged(mLastSelectedPosition)
                          mLastSelectedPosition = position
                      }
                  }
              }
    }

    override fun getItemCount() = earningsDetailsCourseList.size


    class EarningsDetailsViewHolder(val binding: CourseEarningItemViewBinding):
            RecyclerView.ViewHolder(binding.root){
        var view = binding
        var parentCard=binding.parentCard
        var card=binding.downCardView
    }
}