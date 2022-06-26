package com.kapilguru.trainer.student.myClassRoomDetails.studymaterial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.StudentStudyMaterialRecyclerBinding
import com.kapilguru.trainer.student.myClassRoomDetails.model.StudentStudyMaterialResponseApi

class StudentStudyMaterialAdapter(val listener: StudyMaterialListener) : RecyclerView.Adapter<StudentStudyMaterialAdapter.ViewHolder>() {
    private val TAG = "StudyMaterialAdapter"
    private var mTopCategoriesList: List<StudentStudyMaterialResponseApi> = ArrayList()

    fun setData(studentStudyMaterialResponseApi: List<StudentStudyMaterialResponseApi>) {
        mTopCategoriesList = studentStudyMaterialResponseApi
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: StudentStudyMaterialRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onTap(mTopCategoriesList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StudentStudyMaterialRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mTopCategoriesList[position]
    }

    override fun getItemCount(): Int {
        return mTopCategoriesList.size
    }

    interface StudyMaterialListener {
        fun onTap(studentStudyMaterialResponseApi: StudentStudyMaterialResponseApi)
    }
}