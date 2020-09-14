package com.marchosiax.spinner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class Wheel(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var parentWidth = 0
    private var backWheel1Radius = 0f
    private var backWheel2Radius = 0f
    private var wheelRadius = 0f
    private var nailRadius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var textSize = 14f.dp(context)

    private val wheelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }
    private val backWheel1Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#fbc02d")
    }
    private val backWheel2Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#d81b60")
    }
    private val insideShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = shadowShader
    }
    private val nailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }
    private val piePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.CYAN
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG).apply {
        color = Color.WHITE
        textSize = this@Wheel.textSize
        textAlign = Paint.Align.RIGHT
    }
    private var shadowShader: RadialGradient? = null

    private val sections = arrayListOf<PieInternal>().apply {
        add(PieInternal("0", "text1", color = Color.parseColor("#00897B")))
        add(PieInternal("1", "text2", color = Color.parseColor("#5E35B1")))
        add(PieInternal("2", "text3", color = Color.parseColor("#FB8C00")))
        add(PieInternal("3", "text4", color = Color.parseColor("#e53935")))
        add(PieInternal("4", "text5", color = Color.parseColor("#43A047")))
    }

    init {
        init()
    }

    constructor(context: Context) : this(context, null) {
        init()
    }

    private fun init() {
        sections.forEach {
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_android)
            val bitmap = drawable.asBitmap()
            it.icon = bitmap
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawCircle(centerX, centerY, backWheel1Radius, backWheel1Paint)
            drawCircle(centerX, centerY, backWheel2Radius, backWheel2Paint)
            drawCircle(centerX, centerY, wheelRadius, wheelPaint)
            sections.forEach { s ->
                s.color?.let { piePaint.color = it }
                s.pieRect?.let { drawArc(it, s.angle, s.sweepAngle, true, piePaint) }

                val vOffset = s.textCoordinates?.vOffset ?: 0f
                val hOffset = s.textCoordinates?.hOffset ?: 0f
                s.textPath?.let {
                    drawTextOnPath(s.text.truncate(), it, hOffset, vOffset, textPaint)
                }
            }

            insideShadowPaint.shader = getShadowShader()
            drawCircle(centerX, centerY, wheelRadius, insideShadowPaint)

            drawCircle(centerX, centerY, nailRadius, nailPaint)
        }
    }

    private fun getShadowShader(): RadialGradient {
        if (shadowShader == null)
            shadowShader = RadialGradient(
                centerX,
                centerY,
                backWheel2Radius,
                intArrayOf(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                    Color.parseColor("#8F000000")
                ),
                null,
                Shader.TileMode.REPEAT
            )
        return shadowShader!!
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        parentWidth = (parent as View).width
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, width)
        measureWheel()
        calculateSections()
    }

    private fun measureWidth(widthSpec: Int): Int {
        val availableWidth = MeasureSpec.getSize(widthSpec)
        val mode = MeasureSpec.getMode(widthSpec)

        val padding = paddingEnd + paddingStart
        val size = (parentWidth) + padding

        return when (mode) {
            MeasureSpec.EXACTLY -> availableWidth
            MeasureSpec.UNSPECIFIED -> size
            else -> min(availableWidth, size)
        }
    }

    private fun measureHeight(heightSpec: Int): Int {
        val availableHeight = MeasureSpec.getSize(heightSpec)
        val mode = MeasureSpec.getMode(heightSpec)

        val padding = paddingTop + paddingBottom
        val size = padding

        return when (mode) {
            MeasureSpec.EXACTLY -> availableHeight
            MeasureSpec.UNSPECIFIED -> size
            else -> min(availableHeight, size)
        }
    }

    private fun measureWheel() {
        val padding = paddingStart + paddingEnd
        centerX = measuredWidth / 2f
        centerY = measuredHeight / 2f
        backWheel1Radius = measuredWidth / 2f - padding
        backWheel2Radius = backWheel1Radius.percent(96)
        wheelRadius = backWheel1Radius.percent(92)
        nailRadius = measuredWidth.percent(5) / 2f

    }

    private fun calculateSections() {
        val angle = 360f / sections.size
        val angleRadian = angle.toRadian()
        var separatorAngle = 0f
        var pieAngle = 0f
        var textAngle = angleRadian / 2

        for (s in sections) {
            val endX = cos(separatorAngle) * backWheel1Radius + backWheel1Radius
            val endY = sin(separatorAngle) * backWheel1Radius + backWheel1Radius
            separatorAngle += angleRadian

            s.pieRect = RectF(
                measuredHeight.percent(4).toFloat(),
                measuredHeight.percent(4).toFloat(),
                measuredHeight.percent(96).toFloat(),
                measuredHeight.percent(96).toFloat()
            )
            s.angle = pieAngle
            s.sweepAngle = angle
            pieAngle += angle

            val halfAngle = separatorAngle - (angleRadian / 2)
            val startX = (measuredWidth / 2f)
            val startY = (measuredHeight / 2f)
            val nextX = cos(halfAngle) * backWheel1Radius + backWheel1Radius
            val nextY = sin(halfAngle) * backWheel1Radius + backWheel1Radius
            val textPath = Path().apply {
                moveTo(startX, startY)
                lineTo(nextX, nextY)
            }

            val textX = cos(textAngle) * backWheel1Radius / 2 + backWheel1Radius
            val textY = sin(textAngle) * backWheel1Radius / 2 + backWheel1Radius
            s.textCoordinates = Coordinates(
                textX,
                textY,
                -1f,
                -1f,
                wheelRadius.percent(2),
                wheelRadius.percent(15) * -1
            )
            s.textPath = textPath
            textAngle += separatorAngle
        }
    }

    fun addPies(pies: List<SpinnerWheel.Pie>) {
        sections.clear()
        pies.forEach { sections.add(PieInternal(it.id, it.text, color = it.color)) }
        requestLayout()
    }

    fun getItemAngleFrom360(id: String): Int {
        val item = sections.find { it.id == id }
        return item?.angle?.toInt() ?: -1
    }

    fun setTextProperties(typeface: Typeface?, size: Float = 16f) {
        textPaint.apply {
            this.typeface = typeface
            textSize = size.dp(context)
        }
        invalidate()
    }

    fun itemAngle() = 360 / sections.size

    private data class PieInternal(
        var id: String,
        var text: String,
        var icon: Bitmap? = null,
        var color: Int? = null,
        var textCoordinates: Coordinates? = null,
        var textPath: Path? = null,
        var angle: Float = 0f,
        var sweepAngle: Float = 0f,
        var pieRect: RectF? = null
    )

    private data class Coordinates(
        val startX: Float,
        val startY: Float,
        val endX: Float,
        val endY: Float,
        val vOffset: Float = 0f,
        val hOffset: Float = 0f
    )
}

private fun String.truncate(length: Int = 16): String {
    return if (this.length >= length)
        "${substring(0, length)}..."
    else
        this
}

private fun Float.toRadian() = this * (PI / 180).toFloat()