package com.kapilguru.trainer.ui.guestLectures.addGuestLecture

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.android.synthetic.main.spinneritem.view.*

class AddGuestLectureCourseAdapter(context: FragmentActivity?) : ArrayAdapter<CourseResponse>(context!!, R.layout.spinneritem) {
    private val TAG = "AddGuestLectureCourseAdapter"
    var courses = mutableListOf<CourseResponse>()

    fun setCategoryList(courses: List<CourseResponse>) {
        Log.d(TAG,"setCategoryList courses : "+courses.toString())
        this.courses.addAll(courses.toMutableList())
        notifyDataSetChanged()
    }

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.getDropDownView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val view = recycledView ?: LayoutInflater.from(context).inflate(R.layout.spinneritem, parent, false)
        view.aCTVCategoryName.text = courses[position].courseTitle
        return view
    }

    override fun getCount(): Int {
        return courses.size
    }
}