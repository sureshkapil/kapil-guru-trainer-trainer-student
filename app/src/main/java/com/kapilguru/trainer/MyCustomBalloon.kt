package com.kapilguru.trainer

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon

class MyCustomBalloon(var customArrowPosition: Float, var height: Int) : Balloon.Factory() {
    lateinit var view: Balloon

    override fun create(context: Context, lifecycle: LifecycleOwner?): Balloon {
        view = createBalloon(context) {
            setArrowSize(10)
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setArrowPosition(customArrowPosition)
            setLayout(R.layout.baloon_layout)
            setWidthRatio(0.75f)
            setHeight(height)
            setCornerRadius(4f)
            setOverlayPadding(-6f)
            setAlpha(0.8f)
            setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            setBalloonAnimation(BalloonAnimation.CIRCULAR)
            setLifecycleOwner(lifecycle)
        }
        return view
    }


    fun dismiss() {
        view.dismiss()
    }

}