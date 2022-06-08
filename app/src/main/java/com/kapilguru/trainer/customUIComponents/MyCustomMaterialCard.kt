package com.kapilguru.trainer.customUIComponents


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater

import com.google.android.material.card.MaterialCardView
import com.kapilguru.trainer.R


class MyCustomMaterialCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    init {
        val v = LayoutInflater.from(context).inflate(R.layout.random_ui_check, this, true)
    }

}