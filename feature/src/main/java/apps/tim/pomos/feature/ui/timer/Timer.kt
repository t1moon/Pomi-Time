package apps.tim.pomos.feature.ui.timer

import android.os.CountDownTimer
import apps.tim.pomos.feature.ui.MILLIS_IN_SECOND
import apps.tim.pomos.feature.ui.TASK_DURATION_IN_MILLIS
import io.reactivex.subjects.PublishSubject

class Timer {
    enum class TimerState { PLAYED, PAUSED, STARTED, FINISHED, CANCELLED }

    private var state: TimerState = TimerState.FINISHED
    private var remainingTime = TASK_DURATION_IN_MILLIS.toLong()
    private lateinit var timer: CountDownTimer
    /* return timer remaining time in seconds */
    var timeObservable: PublishSubject<Long> = PublishSubject.create()
    /* return timer state */
    var stateObservable: PublishSubject<TimerState> = PublishSubject.create()

    fun changeState() {
        state = when (state) {
            TimerState.FINISHED -> {
                start()
                TimerState.STARTED
            }
            TimerState.STARTED -> {
                pause()
                TimerState.PAUSED
            }
            TimerState.PAUSED -> {
                play()
                TimerState.PLAYED
            }
            TimerState.PLAYED -> {
                pause()
                TimerState.PAUSED
            }
            TimerState.CANCELLED -> {
                start()
                TimerState.STARTED
            }
        }
    }

    private fun play() {
        startTimer()
        stateObservable.onNext(TimerState.PLAYED)
    }

    private fun start() {
        startTimer()
        stateObservable.onNext(TimerState.STARTED)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onFinish() = finish()

            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                timeObservable.onNext(millisUntilFinished / MILLIS_IN_SECOND)
            }
        }
        timer.start()
    }

    private fun pause() {
        timer.cancel()
        stateObservable.onNext(TimerState.PAUSED)
    }

    private fun finish() {
        remainingTime = TASK_DURATION_IN_MILLIS.toLong()
        stateObservable.onNext(TimerState.FINISHED)
        state = TimerState.FINISHED
    }

    fun stop() {
        if (this::timer.isInitialized) {
            timer.cancel()
            remainingTime = TASK_DURATION_IN_MILLIS.toLong()
            stateObservable.onNext(TimerState.CANCELLED)
            state = TimerState.CANCELLED
        }

    }
}
