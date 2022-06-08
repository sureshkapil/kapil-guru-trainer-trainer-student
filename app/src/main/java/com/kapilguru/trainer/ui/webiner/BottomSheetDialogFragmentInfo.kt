package com.kapilguru.trainer.ui.webiner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentBottomSheetDialogInfoBinding

class BottomSheetDialogFragmentInfo : BottomSheetDialogFragment() {
    private val TAG = "BottomSheetDialogFragmentInfo"
    lateinit var binding: FragmentBottomSheetDialogInfoBinding
    lateinit var adapter: BottomSheetInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBottomSheetDialogInfoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setData()
        setTitle()
        setClickListeners()
    }

    private fun setAdapter() {
        adapter = BottomSheetInfoAdapter()
        binding.rvText.adapter = adapter
    }

    private fun setData() {
        val arrayList: ArrayList<String> = arguments?.getStringArrayList(INFO_LIST) as ArrayList<String>
        adapter.setData(arrayList)
    }

    private fun setClickListeners() {
        binding.actvCross.setOnClickListener {
            dismiss()
        }
    }

    fun setTitle(){
        val title = arguments?.getString(TITLE_KEY)
        binding.actvHeading.text = title
    }

    companion object {
        val INFO_LIST = "INFO_LIST"
        val TITLE_KEY = " TITLE"

        @JvmStatic
        fun newInstance(arrayList: ArrayList<String>,title : String) = BottomSheetDialogFragmentInfo().apply {
            arguments = Bundle().apply {
                putStringArrayList(INFO_LIST, arrayList)
                putString(TITLE_KEY,title)
            }
        }
    }
}