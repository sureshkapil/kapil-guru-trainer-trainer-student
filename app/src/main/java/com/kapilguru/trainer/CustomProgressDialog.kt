package com.kapilguru.trainer

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class CustomProgressDialog(context: Context) {
    var m_Dialog = MaterialAlertDialogBuilder(context)
    val m_Message: TextView
    var mDialogBox: AlertDialog

    init {
        val mView = LayoutInflater.from(context).inflate(R.layout.layout_progress_bar, null)
        m_Dialog.setView(mView)
        m_Message = mView.findViewById(R.id.progressBar_text)
        mDialogBox = m_Dialog.create()
    }

    fun showLoadingDialog() {
        m_Message.text = "Loading..."
        mDialogBox.setCancelable(false)
        mDialogBox.show()
    }

    fun dismissLoadingDialog() {
        if (mDialogBox.isShowing) {
            mDialogBox.dismiss()
        }
    }
}
