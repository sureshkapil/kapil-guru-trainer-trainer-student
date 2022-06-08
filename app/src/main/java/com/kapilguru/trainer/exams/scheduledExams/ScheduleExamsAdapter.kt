package com.kapilguru.trainer.exams.scheduledExams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ScheduleExamsItemBinding

class ScheduleExamsAdapter(val scheduleExamsSelectedBatchListener:ScheduleExamsSelectedBatchListener) : RecyclerView.Adapter<ScheduleExamsAdapter.Holder>() {

    var scheduleExamsResponseList = ArrayList<ScheduleExamsResponse>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    inner class Holder(itemView: ScheduleExamsItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val view = itemView
        init {
            view.examBatchCard.setOnClickListener {
                scheduleExamsSelectedBatchListener.onBatchSelected(scheduleExamsResponseList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = DataBindingUtil.inflate<ScheduleExamsItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.schedule_exams_item,
            parent, false
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.model = scheduleExamsResponseList[position]
    }

    override fun getItemCount(): Int = scheduleExamsResponseList.size


    interface ScheduleExamsSelectedBatchListener{
        fun onBatchSelected(scheduleExamsResponse: ScheduleExamsResponse)
    }

}