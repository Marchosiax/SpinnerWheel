package com.marchosiax.spinner

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class SpinnerWheel(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {
        init(context, attrs)
    }

    lateinit var wheel: Wheel

    private fun init(context: Context, attrs: AttributeSet?) {
        View.inflate(context, R.layout.view_spinner_wheel, this)
        wheel = findViewById(R.id.wheel)
        getAttributes(attrs)
    }

    private fun getAttributes(attrs: AttributeSet?) {
        attrs?.let {

        }
    }

    fun addPies(pies: List<Pie>) {
        wheel.addPies(pies)
    }

    fun spin(
        landOnId: String,
        rounds: Int = 5,
        duration: Long = 10000L,
        onSpinFinished: (() -> Unit)? = null
    ) {
        val landItemAngle = wheel.getItemAngleFrom360(landOnId)
        ValueAnimator.ofFloat((360f * rounds) - (landItemAngle + wheel.itemAngle() / 2)).apply {
            setDuration(duration)
            interpolator = WheelInterpolator()
            onAnimationEnd { onSpinFinished?.invoke() }
            addUpdateListener { wheel.rotation = it.animatedValue as Float }
            start()
        }
    }

    data class Pie(
        val id: String,
        val text: String,
        val color: Int
    )

}