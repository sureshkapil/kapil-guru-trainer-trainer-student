package com.kapilguru.trainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.two_button_dialog_fragment.*


class TwoButtonDialog(val twoButtonDialogInteractor:TwoButtonDialogInteractor) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.two_button_dialog_fragment,container)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(DIALOG_SUBTITLE_PARAM,"")?.let {
            tv_subTitle.text = it
        }

        btn_positive.setOnClickListener {
            twoButtonDialogInteractor.onPositiveClick()
            dismiss()
        }

        btn_negative.setOnClickListener {
            twoButtonDialogInteractor.onNegativeClick()
            dismiss()
        }
    }


    companion object {
        fun newInstance(subTitle: String?,twoButtonDialogInteractor:TwoButtonDialogInteractor): TwoButtonDialog {
            val frag = TwoButtonDialog(twoButtonDialogInteractor)
            val args = Bundle()
            args.putString(DIALOG_SUBTITLE_PARAM, subTitle)
            frag.arguments = args
            return frag
        }
    }

}

interface TwoButtonDialogInteractor  {
    fun onNegativeClick()
    fun onPositiveClick()
}