package apps.tim.pomos.feature.ui.timer

import apps.tim.pomos.feature.ui.MILLIS_IN_SECOND
import apps.tim.pomos.feature.ui.REST_DURATION_IN_MINUTE
import apps.tim.pomos.feature.ui.SECONDS_IN_MINUTE
import apps.tim.pomos.feature.ui.WORK_DURATION_IN_MINUTE
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject


class TimerViewModel(private val tasksRepository: TasksRepository, private val timer: Timer) {

    fun getTimerString(): Observable<String> {
        return timer.timeObservable
                .map {
                    val t = Time(it.first / MILLIS_IN_SECOND)
                    "${t.minutes}:${t.seconds}${if (t.seconds == 0) "0" else ""}"
                }
    }

    fun getTimerProgress(): Observable<Int> {
        return timer.timeObservable
                .map {
                    ((it.first / it.second.toFloat()) * 100).toInt()
                }
    }

    fun stopTimer() {
        timer.stop()
    }

    fun timerViewClicked() {
        timer.onClick()
    }

    fun timerViewLongClicked() {
        timer.onLongClick()
    }

    fun defaultTime(work: Boolean): Observable<String> {
        val duration = (if (work) WORK_DURATION_IN_MINUTE else REST_DURATION_IN_MINUTE)
        return Observable.just(Time((duration * SECONDS_IN_MINUTE)))
                .map {
                    "${it.minutes}:${it.seconds}${if (it.seconds == 0) "0" else ""}"
                }
    }

    fun getTimerState(): PublishSubject<Timer.TimerState> {
        return timer.stateObservable
    }

    fun addPomo(id: Long): Single<Unit> {
        return tasksRepository.addPomo(id)
    }

    fun getCompleted(): Flowable<Int> {
        return tasksRepository.getTasks()
                .flatMap {
                    Observable.fromIterable(it)
                            .filter {
                                it.isActive
                            }
                            .map {
                                it.currentPomo
                            }
                            .reduce { t1: Int, t2: Int ->
                                t2 + t1
                            }
                            .toFlowable()
                }
    }

    fun timerViewModeChanged() {
        timer.onModeChanged()
    }

    class Time(input: Int) {
        var minutes: Int = (input / SECONDS_IN_MINUTE)
        var seconds: Int = (input % SECONDS_IN_MINUTE)
    }
}
