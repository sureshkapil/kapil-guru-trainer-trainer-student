package com.kapilguru.trainer.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kapilguru.trainer.R
import kotlinx.android.synthetic.main.layout_progress_bar.*

class DialogFragmentCustom: DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.layout_progress_bar, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar_text.text = "Loading..."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismiss()
        Log.d("ProfileOptionsFragment", "onDestroyView: ")
    }

}