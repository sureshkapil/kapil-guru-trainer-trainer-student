package com.kapilguru.trainer.addStudent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kapilguru.trainer.R
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.android.synthetic.main.item_custom_spinner.view.*

class CourseSpinnerAdapter(ctx: Context, courseResponse: ArrayList<CourseResponse>) : ArrayAdapter<CourseResponse>(ctx, 0, courseResponse) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    fun createItemView(position: Int, recycledView: View?, parent: ViewGroup):View {
        val country = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.item_custom_spinner,
            parent,
            false
        )

        country?.let {
            view.courseTitle.text = country.courseTitle
        }
        return view
    }
}
