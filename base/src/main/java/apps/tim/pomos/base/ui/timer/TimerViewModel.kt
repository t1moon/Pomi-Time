package apps.tim.pomos.base.ui.timer

import apps.tim.pomos.base.PomoApp
import apps.tim.pomos.base.PreferenceHelper
import apps.tim.pomos.base.ui.MILLIS_IN_SECOND
import apps.tim.pomos.base.ui.SECONDS_IN_MINUTE
import apps.tim.pomos.base.ui.tasks.data.TasksRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject


class TimerViewModel(private val tasksRepository: TasksRepository, private val timer: Timer) {

    fun getTimerString(): Observable<String> {
        return timer.timeObservable
                .map {
                    val t = Time(it.first / MILLIS_IN_SECOND)
                    "${t.minutes}:${if (t.seconds < 10) "0" else ""}${t.seconds}"
                }
    }

    fun getTimerProgress(): Observable<Int> {
        return timer.timeObservable
                .map {
                    ((it.first / it.second.toFloat()) * 100).toInt()
                }
    }

    fun timerViewClicked() {
        timer.onClick()
    }

    fun timerViewLongClicked() {
        timer.onLongClick()
    }

    fun defaultTime(work: Boolean): Observable<String> {
        val workDuration = PreferenceHelper.getWorkDuration(PomoApp.instance)
        val restDuration = PreferenceHelper.getRestDuration(PomoApp.instance)

        val duration = (if (work) workDuration else restDuration)
        return Observable.just(Time((duration * SECONDS_IN_MINUTE)))
                .map {
                    "${it.minutes}:${if (it.seconds < 10) "0" else ""}${it.seconds}"
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

    fun refreshTimer() {
        timer.refresh()
    }

    /**
     * @input in minutes
     */
    class Time(input: Int) {
        var minutes: Int = (input / SECONDS_IN_MINUTE)
        var seconds: Int = (input % SECONDS_IN_MINUTE)
    }
}
