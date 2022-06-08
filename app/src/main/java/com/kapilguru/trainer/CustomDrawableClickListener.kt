package com.kapilguru.trainer

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.textfield.TextInputEditText


class CustomDrawableClickListener(
    var textInputEditText: TextInputEditText,
    var title: String, var subTitle: String?="", var lifecycleOwner: LifecycleOwner
) : View.OnTouchListener {


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val DRAWABLE_LEFT: Int = 0;
        val DRAWABLE_TOP: Int = 1;
        val DRAWABLE_RIGHT: Int = 2;
        val DRAWABLE_BOTTOM: Int = 3;

        if (event?.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (textInputEditText.right - textInputEditText.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                val myCustomBalloon = MyCustomBalloon(0.20f, 120)
                myCustomBalloon.create(textInputEditText.context, lifecycleOwner)
                    .showAlignBottom(textInputEditText, 20, 2)
                myCustomBalloon.view.getContentView().findViewById<TextView>(R.id.title).text =
                    title
                myCustomBalloon.view.getContentView()
                    .findViewById<TextView>(R.id.subtitleText).text =
                    subTitle
                true
            }
        }

        return false
    }


}