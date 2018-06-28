package apps.tim.pomos.feature.ui.timer

import android.os.CountDownTimer
import apps.tim.pomos.feature.ui.REST_DURATION_IN_MILLIS
import apps.tim.pomos.feature.ui.WORK_DURATION_IN_MILLIS
import io.reactivex.subjects.PublishSubject

class Timer {
    enum class TimerState { PLAYED, PAUSED, STARTED, FINISHED, CANCELLED }

    private var state: State = StoppedState(this)
    private var mode: Mode = WorkMode(this)

    private var remainingTime = WORK_DURATION_IN_MILLIS.toLong()
    private lateinit var timer: CountDownTimer
    /* return timer remaining time in seconds */
    var timeObservable: PublishSubject<Pair<Int, Int>> = PublishSubject.create()
    /* return timer state */
    var stateObservable: PublishSubject<TimerState> = PublishSubject.create()

    private fun start() {
        startTimer()
        stateObservable.onNext(TimerState.STARTED)
    }

    private fun play() {
        startTimer()
        stateObservable.onNext(TimerState.PLAYED)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(remainingTime, 1000) {
            val totalDuration = if (mode is WorkMode) WORK_DURATION_IN_MILLIS else REST_DURATION_IN_MILLIS
            override fun onFinish() = finish()

            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                timeObservable.onNext(Pair(millisUntilFinished.toInt(), totalDuration))
            }
        }
        timer.start()
    }

    private fun finish() {
        mode.onFinish()
        state = StoppedState(this)
    }

    private fun workFinish() {
        setRestDuration()
        stateObservable.onNext(TimerState.FINISHED)
    }

    private fun restFinish() {
        setWorkDuration()
        stateObservable.onNext(TimerState.CANCELLED)
    }

    private fun setWorkDuration() {
        remainingTime = WORK_DURATION_IN_MILLIS.toLong()
    }

    private fun setRestDuration() {
        remainingTime = REST_DURATION_IN_MILLIS.toLong()
    }


    private fun pause() {
        timer.cancel()
        stateObservable.onNext(TimerState.PAUSED)
    }


    fun stop() {
        if (this::timer.isInitialized) {
            timer.cancel()
            mode.onCancel()
            stateObservable.onNext(TimerState.CANCELLED)
        }
    }

    fun onClick() {
        state.onClick()
    }

    fun onLongClick() {
        state.onLongClick()
    }

    fun changeState(state: State) {
        this.state = state
    }

    fun onModeChanged() {
        mode.onModeChanged()
    }

    abstract class State(val timer: Timer) {
        abstract fun onClick()
        abstract fun onLongClick()
    }

    class PlayingState(timer: Timer) : State(timer) {

        override fun onClick() {
            timer.pause()
            timer.changeState(PausedState(timer))
        }

        override fun onLongClick() {
            timer.stop()
            timer.changeState(StoppedState(timer))
        }
    }

    class PausedState(timer: Timer) : State(timer) {
        override fun onClick() {
            timer.play()
            timer.changeState(PlayingState(timer))
        }
        override fun onLongClick() {
            timer.stop()
            timer.changeState(StoppedState(timer))
        }
    }

    class StoppedState(timer: Timer) : State(timer) {
        override fun onClick() {
            timer.start()
            timer.changeState(PlayingState(timer))
        }
        override fun onLongClick() {}
    }

    abstract class Mode(val timer: Timer) {
        abstract fun onFinish()
        abstract fun onCancel()
        abstract fun onModeChanged()
    }

    class WorkMode(timer: Timer) : Mode(timer) {
        override fun onModeChanged() {
            timer.setRestDuration()
            timer.mode = RestMode(timer)
        }

        override fun onFinish() {
            timer.workFinish()
            timer.mode = RestMode(timer)
        }

        override fun onCancel() {
            timer.setWorkDuration()
            timer.mode = WorkMode(timer)
        }

    }

    class RestMode(timer: Timer) : Mode(timer) {
        override fun onModeChanged() {
            timer.setWorkDuration()
            timer.mode = WorkMode(timer)
        }

        override fun onFinish() {
            timer.restFinish()
            timer.mode = WorkMode(timer)
        }

        override fun onCancel() {
            timer.setWorkDuration()
            timer.mode = WorkMode(timer)
        }

    }


}
