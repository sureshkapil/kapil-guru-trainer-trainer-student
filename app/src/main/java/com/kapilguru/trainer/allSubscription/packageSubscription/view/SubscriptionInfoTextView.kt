package com.kapilguru.trainer.allSubscription.packageSubscription.view

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityPackageSubscriptionInfoBinding
import com.kapilguru.trainer.databinding.ActivityPositionSubscriptionInfoBinding

class SubscriptionInfoTextView : AppCompatTextView {
    lateinit var textView: AppCompatTextView
    lateinit var linearLayout : LinearLayout

    constructor( linearLayout: LinearLayout, context: Context) : super(context){
        this.linearLayout = linearLayout
        setTextLayout(linearLayout)
    }

    constructor(context: Context) : super(context)

    constructor(context:Context, attrs: AttributeSet) : super(context,attrs)

    private fun setTextLayout(linearLayout : LinearLayout){
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 25, 8, 25)
        textView = AppCompatTextView(context)
        textView.layoutParams = layoutParams
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check, 0, 0, 0)
        linearLayout.addView(textView)
    }

    fun setText(text: String) {
        textView.setText(text)
    }

    fun setHtml(html : String){
        textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(html)
        }
        this.textView.setTextColor(resources.getColor(R.color.white))

    }
}