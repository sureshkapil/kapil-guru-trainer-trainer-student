package com.kapilguru.trainer

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.custom_merger_view.view.*

class CustomMergerText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.custom_merger_view, this)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomMergerText)
            val title = resources.getText(typedArray.getResourceId(R.styleable.CustomMergerText_title, R.string.batch_time))
            text_key.text = title
            val titleValue = resources.getText(typedArray.getResourceId(R.styleable.CustomMergerText_value, R.string.batch_time))
            text_value.text = titleValue
            val drawable = typedArray.getDrawable(R.styleable.CustomMergerText_image_src)
            image_src.background = drawable
        }
    }
}