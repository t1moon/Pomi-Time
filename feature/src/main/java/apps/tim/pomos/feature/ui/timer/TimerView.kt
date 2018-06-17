package apps.tim.pomos.feature.ui.timer

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.media.RingtoneManager
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.dipToPx
import apps.tim.pomos.feature.ui.START_ANGLE




class TimerView : View {
    private var bgColor: Int = 0
    private var fgColor: Int = 0
    private var fgColorWhenPause: Int = 0
    private var pauseIconColor: Int = 0
    private lateinit var bgPaint: Paint
    private lateinit var fgPaint: Paint
    private lateinit var pauseIconPaint: Paint
    private lateinit var fgPaint1: Paint

    private lateinit var oval: RectF
    private lateinit var outerOval: RectF
    private lateinit var centerIcon: Rect

    private lateinit var animator: ObjectAnimator

    private var percent: Float = 0.toFloat()

    private var elevationPlay: Float = 0.toFloat()
    private var elevationPause: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(attrs,
                R.styleable.TimerViewStyleable,
                0, 0)

        try {
            bgColor = a.getColor(R.styleable.TimerViewStyleable_bgColor, 0)
            fgColor = a.getColor(R.styleable.TimerViewStyleable_fgColor, 0)
            fgColorWhenPause = a.getColor(R.styleable.TimerViewStyleable_fgColorPause, 0)
            percent = a.getFloat(R.styleable.TimerViewStyleable_percent, 0f)
            elevationPause = resources.dipToPx(a.getFloat(R.styleable.TimerViewStyleable_elevationPause, 0f))
            elevationPlay = resources.dipToPx(a.getFloat(R.styleable.TimerViewStyleable_elevationPlay, 0f))
        } finally {
            a.recycle()
        }
        init()
    }

    private fun init() {
        bgPaint = Paint()
        bgPaint.color = bgColor
        bgPaint.isAntiAlias = true

        fgPaint = Paint()
        fgPaint.color = fgColorWhenPause
        fgPaint.isAntiAlias = true

        fgPaint1 = Paint()
        fgPaint1.color = fgColorWhenPause
        fgPaint1.isAntiAlias = true

        pauseIconPaint = Paint()
        pauseIconColor = PomoApp.color(R.color.timerPause)
        pauseIconPaint.color = pauseIconColor
        pauseIconPaint.isAntiAlias = true

        fgPaint1.style = Paint.Style.STROKE
        fgPaint1.strokeWidth = 7f
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
        val centerIconWidth = (w / 2)

        oval = RectF(paddingLeft + ovalPadding, paddingTop + ovalPadding,
                paddingLeft + wwd - ovalPadding, paddingTop + hhd - ovalPadding)
        outerOval = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), paddingLeft + wwd, paddingTop + hhd)

        centerIcon = Rect(
                (paddingLeft + centerX - centerIconWidth / 2).toInt(),
                (paddingTop + centerY - centerIconWidth / 2).toInt(),
                (paddingLeft + centerX + centerIconWidth / 2).toInt(),
                (paddingTop + centerY + centerIconWidth / 2).toInt()
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.outlineProvider = OutlineProvider(resources = resources, padding = paddingStart)
            this.elevation = elevationPause
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(oval, START_ANGLE, percent * 3.6f, true, fgPaint)
        canvas.drawArc(outerOval, START_ANGLE, 360f, true, fgPaint1)
        val d = resources.getDrawable(R.drawable.ic_play)
        d.bounds = centerIcon
        d.draw(canvas)
    }

    fun play() {
        toggle(true)
    }

    fun pause() {
        toggle(false)
    }

    fun cancel() {
        //TODO
    }

    private fun toggle(play: Boolean) {
        elevate(play)
        fadeBackground(play)
        fadePauseIcon(play)
        playSound(play)
        if (play)
            fillSector()
    }

    private fun fillSector() {
        ValueAnimator.ofFloat(0f, 100f).apply {
            duration = 600
            addUpdateListener {
                percent = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun playSound(play: Boolean) {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        RingtoneManager.getRingtone(context, uri).play()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun fadePauseIcon(play: Boolean) {
        val pauseColor: Int = if (!play)
            pauseIconColor
        else
            Color.TRANSPARENT

        ValueAnimator.ofArgb(pauseIconPaint.color, pauseColor).apply {
            duration = 600
            addUpdateListener {
                pauseIconPaint.color = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun fadeBackground(play: Boolean) {
        val color: Int = if (!play)
            fgColorWhenPause
        else
            fgColor
        ValueAnimator.ofArgb(fgPaint.color, color).apply {
            duration = 600
            addUpdateListener {
                fgPaint.color = it.animatedValue as Int
                fgPaint1.color = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun elevate(play: Boolean) {
        val elevateTo: Float = if (play)
            elevationPlay
        else
            elevationPause

        ValueAnimator.ofFloat(elevation, elevateTo).apply {
            duration = 600
            addUpdateListener {
                elevation = it.animatedValue as Float
            }
            if (play)
                interpolator = BounceInterpolator()
            start()
        }
    }

}
