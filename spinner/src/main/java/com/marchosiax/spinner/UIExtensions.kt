package com.marchosiax.spinner

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import java.io.ByteArrayOutputStream


/***************** Extensions for subclasses of "View" *****************/
internal fun View.hideKeyboard() {
    val inputMethodService = context.getSystemService(Context.INPUT_METHOD_SERVICE)
    if (inputMethodService != null)
        (inputMethodService as InputMethodManager).hideSoftInputFromWindow(
            windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
}

internal fun View.requestFocusAndShowKeyboard() {
    requestFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

internal fun View.invisible() {
    visibility = View.INVISIBLE
}

internal fun View.gone() {
    visibility = View.GONE
}

internal fun View.visible() {
    visibility = View.VISIBLE
}

internal fun View.enable() {
    isEnabled = true
}

internal fun View.disable() {
    isEnabled = false
}

internal fun View.locationOnScreen(): Point {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return Point(location[0], location[1])
}

internal fun View.centerLocationOnScreen(): Point {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return Point(width / 2 + location[0], height / 2 + location[1])
}


internal fun ImageView.setGrayScale(boolean: Boolean) {
    if (boolean) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val cf = ColorMatrixColorFilter(matrix)
        colorFilter = cf
        imageAlpha = 128
    } else {
        colorFilter = null
        imageAlpha = 255
    }
}

internal fun Editable?.set(textToSet: String?) {
    this ?: return
    textToSet ?: return
    clear()
    append(textToSet)
}

/***************** Extensions for subclasses of "Context" *****************/
@ColorInt
internal fun Context.getColorByAttribute(resId: Int): Int {
    val typedValue = TypedValue()
    val theme = theme
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

internal fun Context.getAppSettingsIntent(): Intent = Intent().apply {
    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    addCategory(Intent.CATEGORY_DEFAULT)
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
    data = Uri.fromParts("package", packageName, null)
}

internal fun Context.drawableToByteArray(drawableId: Int): ByteArray {
    val stream = ByteArrayOutputStream()
    val logo = BitmapFactory.decodeResource(resources, drawableId)
    logo.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

internal fun Context.getStringByResName(name: String?): String? =
    resources.getString(resources.getIdentifier(name, "string", packageName))

internal fun Context.getDrawableByResName(name: String?): Drawable? = try {
    ContextCompat.getDrawable(this, resources.getIdentifier(name, "drawable", packageName))
} catch (e: Exception) {
    e.printStackTrace()
    null
}

internal fun Context.getDrawableIdByResName(name: String?): Int =
    resources.getIdentifier(name, "drawable", packageName)

/***************** Extensions for other classes *****************/
internal fun Int.dp(context: Context): Int =
    (this * context.resources.displayMetrics.density).toInt()

internal fun Float.dp(context: Context): Float = this * context.resources.displayMetrics.density

internal fun Int.percent(n: Int) = times(n) / 100

internal fun Int.percent(n: Float) = times(n) / 100

internal fun Float.percent(n: Int) = times(n) / 100

internal fun Drawable?.asBitmap(): Bitmap? {
    if (this == null)
        return null

    var drawable: Drawable = this

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        drawable = DrawableCompat.wrap(this).mutate()
    }

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}