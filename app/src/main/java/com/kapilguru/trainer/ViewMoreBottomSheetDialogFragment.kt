package com.kapilguru.trainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kapilguru.trainer.databinding.ViewMoreBottomSheetBinding
import com.kapilguru.trainer.ui.webiner.BottomSheetInfoAdapter

class ViewMoreBottomSheetDialogFragment: BottomSheetDialogFragment() {
    private val TAG = "BottomSheetDialogFragmentInfo"
    lateinit var binding: ViewMoreBottomSheetBinding
    lateinit var adapter: BottomSheetInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogFixedHeight)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (dialog as? BottomSheetDialog)?.let {
            it.behavior.peekHeight = 500
        }

        binding = ViewMoreBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    override fun onStart() {
        super.onStart()
//        dialog?.also {
//            val bottomSheet = it.findViewById<View>(R.id.design_bottom_sheet)
//            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//            val behavior = BottomSheetBehavior.from<View>(bottomSheet)
//            behavior.peekHeight = 500 //replace to whatever you want
//            view?.requestLayout()
//        }
    }

    private fun setData() {
        binding.viewMore.text = arguments?.getString(TEXT_INFO)?.let { it.fromBase64() }
    }


    companion object {
        val TEXT_INFO = "text_info"
        @JvmStatic
        fun newInstance(text : String) = ViewMoreBottomSheetDialogFragment().apply {
            arguments = Bundle().apply {
                putString(TEXT_INFO, text)
            }
        }
    }
}