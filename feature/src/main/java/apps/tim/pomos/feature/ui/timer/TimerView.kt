package apps.tim.pomos.feature.ui.timer

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import apps.tim.pomos.feature.R


class TimerView : View {
    private var bgColor: Int = 0
    private var fgColor: Int = 0
    private var bgPaint: Paint? = null
    private var fgPaint: Paint? = null
    private var fgPaint1: Paint? = null
    private var oval: RectF? = null
    private var outerOval: RectF? = null

    private var animator: ObjectAnimator? = null
    private var sectors: Int = 0

    private var percent: Float = 0.toFloat()

    private var startAngle: Float = 0.toFloat()

    private val elevationPushed: Float =
            resources.getDimensionPixelSize(R.dimen.timer_elevation_pushed).toFloat()
    private val elevationPulled: Float =
            resources.getDimensionPixelSize(R.dimen.timer_elevation_pulled).toFloat()

    private var pushed: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(attrs,
                R.styleable.SectorProgressView,
                0, 0)

        try {
            bgColor = a.getColor(R.styleable.SectorProgressView_bgColor, -0x1a1a1b)
            fgColor = a.getColor(R.styleable.SectorProgressView_fgColor, -0x89a4)
            percent = a.getFloat(R.styleable.SectorProgressView_percent, 0f)
            startAngle = a.getFloat(R.styleable.SectorProgressView_startAngle, 0f) + 270

        } finally {
            a.recycle()
        }

        init()
    }

    private fun init() {
        bgPaint = Paint()
        bgPaint!!.color = bgColor
        bgPaint!!.isAntiAlias = true

        fgPaint = Paint()
        fgPaint!!.color = fgColor
        fgPaint!!.isAntiAlias = true

        fgPaint1 = Paint()
        fgPaint1!!.color = fgColor
        fgPaint1!!.isAntiAlias = true

        fgPaint1!!.style = Paint.Style.STROKE
        fgPaint1!!.strokeWidth = 7f
        sectors = 1
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val xpad = (paddingLeft + paddingRight).toFloat()
        val ypad = (paddingBottom + paddingTop).toFloat()

        val wwd = w.toFloat() - xpad
        val hhd = h.toFloat() - ypad

        val ovalPadding = (w / 15).toFloat()
        oval = RectF(paddingLeft + ovalPadding, paddingTop + ovalPadding,
                paddingLeft + wwd - ovalPadding, paddingTop + hhd - ovalPadding)
        outerOval = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), paddingLeft + wwd, paddingTop + hhd)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.outlineProvider = OutlineProvider(resources = resources, padding = paddingStart)
            this.elevation = elevationPulled
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(oval!!, startAngle, percent * 3.6f, true, fgPaint!!)
        canvas.drawArc(outerOval!!, startAngle, 360f, true, fgPaint1!!)
    }


    fun getBgColor(): Int {
        return bgColor
    }

    fun setBgColor(bgColor: Int) {
        this.bgColor = bgColor
        refreshTheLayout()
    }

    fun getFgColor(): Int {
        return fgColor
    }

    fun setFgColor(fgColor: Int) {
        this.fgColor = fgColor
        refreshTheLayout()
    }


    private fun refreshTheLayout() {
        invalidate()
        requestLayout()
    }

    fun getStartAngle(): Float {
        return startAngle
    }

    fun setStartAngle(startAngle: Float) {
        this.startAngle = startAngle + 270
        invalidate()
        requestLayout()
    }

    fun getPercent(): Float {
        return percent
    }

    fun setPercent(percent: Float) {
        this.percent = percent
        invalidate()
        requestLayout()
    }

    @JvmOverloads
    fun animateIndeterminate(durationOneCircle: Int = 800,
                             interpolator: TimeInterpolator? = AccelerateDecelerateInterpolator()) {
        animator = ObjectAnimator.ofFloat(this, "startAngle", getStartAngle(), getStartAngle() + 360)
        if (interpolator != null) animator!!.interpolator = interpolator
        animator!!.duration = durationOneCircle.toLong()
        animator!!.repeatCount = ValueAnimator.INFINITE
        animator!!.repeatMode = ValueAnimator.RESTART
        animator!!.start()
    }

    fun stopAnimateIndeterminate() {
        if (animator != null) {
            animator!!.cancel()
            animator = null
        }
    }

    fun toggle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pushed = !pushed
            var elevateTo: Float = if (pushed)
                elevationPushed
            else
                elevationPulled

            ValueAnimator.ofFloat(elevation, elevateTo).apply {
                duration = 600
                addUpdateListener {
                    elevation = it.animatedValue as Float
                }
                start()
            }
        }
    }
}
