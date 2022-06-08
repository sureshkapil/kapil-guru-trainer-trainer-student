package com.kapilguru.trainer.student.homeActivity.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.StudentDashBoardItemViewBinding
import com.kapilguru.trainer.student.homeActivity.models.StudentDashBoardItem

class StudentDashBoardAdapter(var onItemClickedForHome: OnItemClickedForHome) : RecyclerView.Adapter<StudentDashBoardAdapter.HomeViewHolder>() {

     var homeItems: ArrayList<StudentDashBoardItem> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return homeItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = StudentDashBoardItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.view.itemData = homeItems[position]
        holder.layoutView.setOnClickListener {
            onItemClickedForHome.onItemClick(position)
        }
    }

    class HomeViewHolder(itemView: StudentDashBoardItemViewBinding) : RecyclerView.ViewHolder(itemView.root) {
        var view = itemView
        var layoutView = itemView.lLayoutHome
    }

    interface OnItemClickedForHome {
        fun onItemClick(position: Int)
    }
}