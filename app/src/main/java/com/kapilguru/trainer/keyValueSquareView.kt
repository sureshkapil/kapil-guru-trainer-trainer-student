package com.kapilguru.trainer

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.key_value_square_view.view.*

class keyValueSquareView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.key_value_square_view, this)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,R.styleable.keyValueSquareView)
            val keyTitle = resources.getText(typedArray.getResourceId(R.styleable.keyValueSquareView_keyValueSquareView_title,R.string.batch_time))
            text_key.text = keyTitle
            val keyValue = resources.getText(typedArray.getResourceId(R.styleable.keyValueSquareView_keyValueSquareView_title,R.string.batch_time))
            text_value.text = keyValue
        }
    }
}