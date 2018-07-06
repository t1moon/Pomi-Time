package apps.tim.pomos.base.ui.timer.viewmodel

import apps.tim.pomos.base.MILLIS_IN_MINUTE
import apps.tim.pomos.base.MILLIS_IN_SECOND
import apps.tim.pomos.base.data.repository.TasksRepository
import apps.tim.pomos.base.preference.SettingsPreference
import apps.tim.pomos.base.ui.base.BaseViewModel
import apps.tim.pomos.base.ui.timer.Timer
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
                    val t = ClockTime(it.first)
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
        val workDuration = SettingsPreference.workDuration
        val restDuration = SettingsPreference.restDuration

        val duration = (if (work) workDuration else restDuration)
        return Observable.just(ClockTime((duration * MILLIS_IN_MINUTE)))
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

    class ClockTime(inputInMillis: Int) {
        var minutes: Int = (inputInMillis / MILLIS_IN_MINUTE)
        var seconds: Int = (inputInMillis % MILLIS_IN_MINUTE) / MILLIS_IN_SECOND
    }
}
