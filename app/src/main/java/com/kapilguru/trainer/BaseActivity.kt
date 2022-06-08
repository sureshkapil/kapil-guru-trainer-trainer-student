package com.kapilguru.trainer

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    fun setActionbarBackListener(context: AppCompatActivity, view: View, title: String, showInfo: Boolean = false, toolbarListener: ToolBarClickListener? = null) {
        val linearView = view as LinearLayout
        val activityName = view.findViewById<TextView>(R.id.tv_activity_name)
        val backIcon = view.findViewById<ImageView>(R.id.aciv_back)
        val infoIcon = view.findViewById<ImageView>(R.id.aciv_info)
        activityName.text = title
        backIcon.setOnClickListener {
            context.finish()
        }
        if (showInfo) {
            infoIcon.visibility = View.VISIBLE
            infoIcon.setOnClickListener {
                toolbarListener?.onShowInfoClicked()
            }
        }
    }


    interface ToolBarClickListener {
        fun onShowInfoClicked()
    }
}