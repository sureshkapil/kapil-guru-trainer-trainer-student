package com.kapilguru.trainer.ui.refund

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentRefundBottomSheetBinding
import com.kapilguru.trainer.ui.refund.model.RefundData


class RefundBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var refundBottomSheetBinding: FragmentRefundBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        refundBottomSheetBinding = DataBindingUtil.inflate<FragmentRefundBottomSheetBinding>(inflater, R.layout.fragment_refund_bottom_sheet, container, false)
        refundBottomSheetBinding.lifecycleOwner = this
        refundBottomSheetBinding.aCTVCloseImage.setOnClickListener { this.dismiss() }
        return refundBottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments?.getSerializable("refundData") as RefundData
        refundBottomSheetBinding.refundModelData = bundle
    }

    companion object {
        @JvmStatic
        fun newInstance(refundData: RefundData) =
            RefundBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("refundData", refundData)
                }
            }


        /*
        * later move to common Adapter files
        * */
      /*  @JvmStatic
        @BindingAdapter("id")
        fun TextView.settingStringValue(id: Int) {
            this.text = id.toString()
        }*/

    }

}
