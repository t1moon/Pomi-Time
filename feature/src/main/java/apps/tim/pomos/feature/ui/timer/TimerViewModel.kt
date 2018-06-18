package apps.tim.pomos.feature.ui.timer

import apps.tim.pomos.feature.ui.SECONDS_IN_MINUTE
import apps.tim.pomos.feature.ui.TASK_DURATION_IN_MINUTE
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject


class TimerViewModel(val tasksRepository: TasksRepository, val timer: Timer) {

    fun getTimerString() : Observable<String> {
        return timer.timeObservable
                .map {
                    val t = Time(it)
                    "${t.minutes}:${t.seconds}${if (t.seconds == 0) "0" else ""}"
                }
    }

    fun getTimerProgress() : Observable<Int> {
        return timer.timeObservable
                .map {
                    it.toInt()
                }
    }

    fun stopTimer() {
        timer.stop()
    }

    fun timerViewClicked() {
        timer.changeState()
    }

    fun timerViewLongClicked() {
        timer.stop()
    }

    fun defaultTime(): Observable<String> {
        return Observable.just(Time((TASK_DURATION_IN_MINUTE * SECONDS_IN_MINUTE).toLong()))
                .map {
                    "${it.minutes}:${it.seconds}${if (it.seconds == 0) "0" else ""}"
                }
    }

    fun getTimerState() : PublishSubject<Timer.TimerState> {
        return timer.stateObservable
    }

    fun addPomo(id: Long) : Single<Unit> {
        return tasksRepository.addPomo(id)
    }

    class Time(input: Long) {
        var minutes: Int = (input / SECONDS_IN_MINUTE).toInt()
        var seconds: Int = (input % SECONDS_IN_MINUTE).toInt()
    }
}
