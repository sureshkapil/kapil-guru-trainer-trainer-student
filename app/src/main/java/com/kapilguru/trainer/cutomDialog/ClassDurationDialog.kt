package com.kapilguru.trainer.cutomDialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.kapilguru.trainer.R
import kotlinx.android.synthetic.main.dialog_class_duration.view.*

class ClassDurationDialog : DialogFragment(),AdapterToDialogListner {
    lateinit var arrayListDurationModel: ArrayList<DurationModel>

    companion object {
        const val TAG = "SimpleDialog"
        fun newInstance(arrayListDurationModel: ArrayList<DurationModel>): ClassDurationDialog {
            val fragment = ClassDurationDialog()
            val args = Bundle()
            args.putParcelableArrayList("info",arrayListDurationModel)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         arguments?.let{ it ->
           arrayListDurationModel=  it.getParcelableArrayList<DurationModel>("info") as ArrayList<DurationModel>
        }
    }

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.dialog_class_duration, container, false)
        dialog?.window?.let {
           it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
//            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val width = getWidthMetrics() - 70
            val height =  ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    private fun setupView(view: View) {
        val adapter = ClassDurationAdapter()
        view.class_duration_rView.adapter = adapter
        adapter.setData(arrayListDurationModel, this)
    }

    fun getWidthMetrics(): Int {
        val screenWidth = resources.displayMetrics.widthPixels
       return screenWidth
    }

    fun getHeightMetrics(): Int {
        val screenHeight = resources.displayMetrics.heightPixels

        return screenHeight
    }

    override fun getPosition(position: Int) {
        setFragmentResult("abc",bundleOf("resultKey" to position))
        dismiss()
    }
}
