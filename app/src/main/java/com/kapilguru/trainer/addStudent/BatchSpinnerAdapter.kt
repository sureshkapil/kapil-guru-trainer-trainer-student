package com.kapilguru.trainer.addStudent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kapilguru.trainer.R
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListResponse
import kotlinx.android.synthetic.main.item_custom_spinner.view.*

class BatchSpinnerAdapter(ctx: Context, batchListResponse: ArrayList<BatchListResponse>) : ArrayAdapter<BatchListResponse>(ctx, 0, batchListResponse) {

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
            view.courseTitle.text = country.batchCode
        }
        return view
    }
}
