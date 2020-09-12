package com.marchosiax.spinner

import android.view.animation.Interpolator
import kotlin.math.pow

class WheelInterpolator : Interpolator {

    override fun getInterpolation(x: Float): Float {
        //return sqrt(1 - (x - 1).pow(2))
        //return if (x < 0.5729) 8 * x.pow(5) else 1 - (-2 * (x-0.07499f) + 2).pow(5f) / 2
        //return if(x<0.3378) (0.5*(0.87-x).pow(5)).toFloat() else ((0.6*x.pow(2))+0.38884).toFloat()
        return 1 - (1 - x).pow(3)
    }

}