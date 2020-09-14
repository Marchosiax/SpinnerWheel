package com.marchosiax.spinner

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController

private const val DEFAULT_DURATION = 500L

fun Context.loadLayoutAnimation(animId: Int): LayoutAnimationController {
    return AnimationUtils.loadLayoutAnimation(this, animId)
}

fun Context.loadAnimation(animId: Int): Animation? {
    return AnimationUtils.loadAnimation(this, animId)
}

fun View.fadeIn(duration: Long = DEFAULT_DURATION) {
    val fade = ValueAnimator.ofFloat(0F, 1F).apply {
        setDuration(duration)
        addUpdateListener { alpha = it.animatedValue as Float }
    }
    fade.start()
    this.visible()
}

fun View.fadeOut(duration: Long = DEFAULT_DURATION) {
    val fade = ValueAnimator.ofFloat(1F, 0F).apply {
        setDuration(duration)
        addUpdateListener { alpha = it.animatedValue as Float }
    }
    fade.start()
    this.invisible()
}

fun Animation.onAnimationEnd(action: (Animation?) -> Unit) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }

        override fun onAnimationStart(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            action(animation)
        }
    })
}

fun Animator.onAnimationStart(action: (Animator?) -> Unit) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {

        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {
            action(animation)
        }

    })
}

fun Animator.onAnimationEnd(action: (Animator?) -> Unit) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            action(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {

        }

    })
}