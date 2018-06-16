package apps.tim.pomos.feature.ui.timer

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import apps.tim.pomos.feature.R
import android.media.RingtoneManager




class TimerView : View {
    private var bgColor: Int = 0
    private var fgColor: Int = 0
    private var fgColorPause: Int = 0
    private var pauseColor: Int = 0
    private var bgPaint: Paint? = null
    private var fgPaint: Paint? = null
    private var pausePaint: Paint? = null
    private var fgPaint1: Paint? = null

    private var oval: RectF? = null
    private var outerOval: RectF? = null
    private var pauseOvalLeft: RectF? = null
    private var pauseOvalRight: RectF? = null

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
            fgColorPause = a.getColor(R.styleable.SectorProgressView_fgColorPause, -0x89a4)
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
        fgPaint!!.color = fgColorPause
        fgPaint!!.isAntiAlias = true

        fgPaint1 = Paint()
        fgPaint1!!.color = fgColorPause
        fgPaint1!!.isAntiAlias = true

        pausePaint = Paint()
        pauseColor = resources.getColor(R.color.timerPause)
        pausePaint!!.color = pauseColor
        pausePaint!!.isAntiAlias = true

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
        val centerX = (w / 2).toFloat()
        val centerY = (h / 2).toFloat()
        val pauseOvalPaddingCenter = (w / 15).toFloat()
        val pauseHeight = (w / 4).toFloat()
        val pauseWidth = (w / 6).toFloat()

        oval = RectF(paddingLeft + ovalPadding, paddingTop + ovalPadding,
                paddingLeft + wwd - ovalPadding, paddingTop + hhd - ovalPadding)
        outerOval = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), paddingLeft + wwd, paddingTop + hhd)

        pauseOvalLeft = RectF(
                paddingLeft + centerX - pauseOvalPaddingCenter - pauseWidth,
                paddingTop + centerY - pauseHeight,
                paddingLeft + centerX - pauseOvalPaddingCenter,
                paddingTop + centerY + pauseHeight)

        pauseOvalRight = RectF(
                paddingLeft + centerX + pauseOvalPaddingCenter,
                paddingTop + centerY - pauseHeight,
                paddingLeft + centerX + pauseOvalPaddingCenter + pauseWidth,
                paddingTop + centerY + pauseHeight)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.outlineProvider = OutlineProvider(resources = resources, padding = paddingStart)
            this.elevation = elevationPulled
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(oval!!, startAngle, percent * 3.6f, true, fgPaint!!)
        canvas.drawArc(outerOval!!, startAngle, 360f, true, fgPaint1!!)
        canvas.drawRoundRect(pauseOvalLeft!!, 10f, 10f, pausePaint!!)
        canvas.drawRoundRect(pauseOvalRight!!, 10f, 10f, pausePaint!!)
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
                if (pushed)
                    interpolator = BounceInterpolator()
                start()
            }
        }
        var color: Int = if (!pushed)
            fgColorPause
        else
            fgColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ValueAnimator.ofArgb(fgPaint!!.color, color).apply {
                duration = 600
                addUpdateListener {
                    fgPaint!!.color = it.animatedValue as Int
                    fgPaint1!!.color = it.animatedValue as Int
                    invalidate()
                }
                start()
            }
        }

        var pauseColor: Int = if (!pushed)
            pauseColor
        else
            Color.TRANSPARENT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ValueAnimator.ofArgb(pausePaint!!.color, pauseColor).apply {
                duration = 600
                addUpdateListener {
                    pausePaint!!.color = it.animatedValue as Int
                    invalidate()
                }
                start()
            }
        }

        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        RingtoneManager.getRingtone(context, uri).play()
    }
}
