package apps.tim.pomos.base.ui.timer

import apps.tim.pomos.base.PomoApp
import apps.tim.pomos.base.PreferenceHelper
import apps.tim.pomos.base.data.TasksRepository
import apps.tim.pomos.base.ui.MILLIS_IN_SECOND
import apps.tim.pomos.base.ui.SECONDS_IN_MINUTE
import apps.tim.pomos.base.ui.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class TimerViewModel(private val tasksRepository: TasksRepository, private val timer: Timer)
    : BaseViewModel() {

    fun addPomo(id: Long) =
            tasksRepository.addPomo(id)

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

    fun getTimerState(): PublishSubject<Timer.TimerState> {
        return timer.stateObservable
    }

    fun timerViewModeChanged() {
        timer.onModeChanged()
    }

    fun refreshTimer() {
        timer.refresh()
    }

    fun getDefaultTime(work: Boolean): Observable<String> {
        val workDuration = PreferenceHelper.getWorkDuration(PomoApp.instance)
        val restDuration = PreferenceHelper.getRestDuration(PomoApp.instance)

        val duration = (if (work) workDuration else restDuration)
        return Observable.just(Time((duration * SECONDS_IN_MINUTE)))
                .map {
                    "${it.minutes}:${if (it.seconds < 10) "0" else ""}${it.seconds}"
                }
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

    class Time(inputInMinutes: Int) {
        var minutes: Int = (inputInMinutes / SECONDS_IN_MINUTE)
        var seconds: Int = (inputInMinutes % SECONDS_IN_MINUTE)
    }
}
