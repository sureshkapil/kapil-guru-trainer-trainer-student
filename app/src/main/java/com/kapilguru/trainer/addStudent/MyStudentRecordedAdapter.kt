package com.kapilguru.trainer.addStudent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.coupons.AllCouponsResponseListApi
import com.kapilguru.trainer.coupons.CouponsAdapter
import com.kapilguru.trainer.databinding.AdapterCouponListItemBinding
import com.kapilguru.trainer.databinding.CoursesListItemBinding
import com.kapilguru.trainer.databinding.RecordedStudentListItemBinding
import com.kapilguru.trainer.databinding.StudentRecordedCoursesItemBinding

class MyStudentRecordedAdapter : RecyclerView.Adapter<MyStudentRecordedAdapter.Holder>() {

    var listItem: ArrayList<MyStudentsRecordedStudyMaterialsResponseApi> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(courseListItem: RecordedStudentListItemBinding) : RecyclerView.ViewHolder(courseListItem.root) {
        var binding = courseListItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecordedStudentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.viewModel = listItem[position]
    }

    override fun getItemCount(): Int = listItem.size


}