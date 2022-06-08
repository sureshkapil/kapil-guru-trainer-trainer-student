package com.kapilguru.trainer.student.homeActivity.studentTestimonials

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.StudentTestimonialsItemBinding

import com.kapilguru.trainer.student.homeActivity.models.DashBoardViewPagerItem

class StudentTestimonialPagerAdapter(val cardClickListener: CardClickListener) : RecyclerView.Adapter<StudentTestimonialPagerAdapter.ViewHolder>() {

    private var homeViewPagerItems = mutableListOf<DashBoardViewPagerItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = StudentTestimonialsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.view.dataView = homeViewPagerItems[position]
    }

    override fun getItemCount(): Int {
        return 10
    }

    fun setViewPagerData(homeViewPagerItems: MutableList<DashBoardViewPagerItem>) {
        this.homeViewPagerItems = homeViewPagerItems
        notifyDataSetChanged()
    }

   inner class ViewHolder(itemView: StudentTestimonialsItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val view = itemView

       init {
//           itemView.card.setOnClickListener {
//               cardClickListener.onCardClick()
//           }
       }
    }

    interface CardClickListener{
        fun onCardClick()
    }

}
