package com.kapilguru.trainer

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.custom_key_value_view.view.*


class KeyValueText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val binding: CustomKeyValueViewBinding = DataBindingUtil.inflate(inflater, R.layout.custom_key_value_view, this, true)
        inflate(getContext(), R.layout.custom_key_value_view, this)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.KeyValueText)

            val title = resources.getText(typedArray.getResourceId(R.styleable.KeyValueText_key_title, R.string.batch_time))
            text_key.text = title

            val titleValue = resources.getText(typedArray.getResourceId(R.styleable.KeyValueText_key_value, R.string.batch_time))
            text_value.text = titleValue
        }
    }
}