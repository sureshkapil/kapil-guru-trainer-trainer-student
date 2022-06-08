package com.kapilguru.trainer.ui.courses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.ui.courses.addcourse.models.CategoryApiData
import kotlinx.android.synthetic.main.spinneritem.view.*

class CategoryBaseAdapter(context: FragmentActivity?) : ArrayAdapter<CategoryApiData>(context!!, R.layout.spinneritem) {

    var categoyies = mutableListOf<CategoryApiData>()

    fun setCategoryList(categoyies: List<CategoryApiData>) {
        this.categoyies.addAll(categoyies.toMutableList())

        notifyDataSetChanged()
    }

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.getDropDownView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val view = recycledView ?: LayoutInflater.from(context).inflate(
                R.layout.spinneritem,
                parent,
                false
        )


        view.aCTVCategoryName.text = categoyies[position].categoryName

        return view
    }

    override fun getCount(): Int {
        return categoyies.size
    }

}