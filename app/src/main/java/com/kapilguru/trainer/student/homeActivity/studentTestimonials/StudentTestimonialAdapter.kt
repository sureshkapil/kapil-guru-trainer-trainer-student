package com.kapilguru.trainer.student.homeActivity.studentTestimonials

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.StudentTestimonialsItemBinding

import com.kapilguru.trainer.student.homeActivity.studentTestimonials.model.StudentTestimonialResData

/*showFullText - true -> show complete review even if it is big*/
class StudentTestimonialAdapter(val showFullText : Boolean) : RecyclerView.Adapter<StudentTestimonialAdapter.ViewHolder>() {

    private var mStudentTestimonialsList = ArrayList<StudentTestimonialResData>()

    fun setStudentTestimonialsList(list: ArrayList<StudentTestimonialResData>) {
        this.mStudentTestimonialsList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: StudentTestimonialsItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            if(showFullText){
                val actvReviewParas = binding.actvReview.layoutParams
                actvReviewParas.height = LinearLayout.LayoutParams.WRAP_CONTENT
                binding.actvReview.layoutParams = actvReviewParas
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = StudentTestimonialsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mStudentTestimonialsList[position]
    }

    override fun getItemCount(): Int {
        return mStudentTestimonialsList.size
    }
}
