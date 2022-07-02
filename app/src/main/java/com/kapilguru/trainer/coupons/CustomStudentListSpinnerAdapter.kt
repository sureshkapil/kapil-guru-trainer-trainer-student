package com.kapilguru.trainer.coupons

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kapilguru.trainer.R
import kotlinx.android.synthetic.main.item_custom_spinner.view.*

class CustomStudentListSpinnerAdapter(ctx: Context, studentModelResponseApi: ArrayList<StudentModelResponseApi>) : ArrayAdapter<StudentModelResponseApi>(ctx, 0, studentModelResponseApi) {


    var studentModelResponseApi: ArrayList<StudentModelResponseApi> = ArrayList()
    get(){
        return field
    }
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    fun createItemView(position: Int, recycledView: View?, parent: ViewGroup):View {
        val student = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.item_custom_spinner,
            parent,
            false
        )

        student?.let {
            view.courseTitle.text = student.studentName
        }
        return view
    }
}
