package com.kapilguru.trainer.student.homeActivity.trendingWebinars

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.StudentAllTrendingWebinarsItemViewBinding
import com.kapilguru.trainer.databinding.StudentTrendingWebinarsItemViewBinding
import com.kapilguru.trainer.student.homeActivity.models.AllWebinarsApi


// isFrom false is DashBoard/TrendingWebinars,viewType-> 0
// isFrom true from AllTrendingWebinars, viewType-> 1
class StudentTrendingWebinarAdapter(var onItemClick: TrendingWebinarCardClick, var isFrom: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var upComingScheduleApiList = listOf<AllWebinarsApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(val itemTodayScheduleBinding: StudentTrendingWebinarsItemViewBinding) : RecyclerView.ViewHolder(itemTodayScheduleBinding.root) {
        var view = itemTodayScheduleBinding
        init {
            view.card.setOnClickListener {
                onItemClick.onTrendingWebinarCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.btnKnowMore.setOnClickListener {
                onItemClick.onTrendingWebinarCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.shareIcon.setOnClickListener {
                onItemClick.onShareClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
        }

    }

    inner class AllTrendingHolder(val studentAllTrendingWebinarsItemViewBinding: StudentAllTrendingWebinarsItemViewBinding) : RecyclerView.ViewHolder(studentAllTrendingWebinarsItemViewBinding.root) {
        var view = studentAllTrendingWebinarsItemViewBinding
        init {
            view.root.setOnClickListener {
                onItemClick.onTrendingWebinarCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.btnKnowMore.setOnClickListener {
                onItemClick.onTrendingWebinarCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.shareIcon.setOnClickListener {
                onItemClick.onShareClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
        }
    }


    override fun getItemViewType(position: Int): Int = if (isFrom) 1 else 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = StudentTrendingWebinarsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Holder(view)
        } else {
            val view = StudentAllTrendingWebinarsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AllTrendingHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as Holder).view.model = upComingScheduleApiList[position]
        } else {
            (holder as AllTrendingHolder).view.model = upComingScheduleApiList[position]
        }
    }

    override fun getItemCount(): Int {
        return upComingScheduleApiList.size
    }


    interface TrendingWebinarCardClick {
        fun onTrendingWebinarCardClick(webinarDetails: AllWebinarsApi)
        fun onShareClick(webinarDetails: AllWebinarsApi)
    }


}
