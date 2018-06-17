package apps.tim.pomos.feature.ui.timer

import apps.tim.pomos.feature.ui.SECONDS_IN_MINUTE
import apps.tim.pomos.feature.ui.TASK_DURATION
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Timer {
    private val taskDuration: Long = SECONDS_IN_MINUTE * TASK_DURATION.toLong()
    private var remainingTime: Long = taskDuration
    private lateinit var observable: Observable<String>


    fun start() : Observable<String> {
        observable = Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil { it > taskDuration - remainingTime }
                .doOnEach { remainingTime -= 1 }
                .map {
                    val t = Time(remainingTime)
                    "${t.minutes}:${t.seconds}"
                }
        return observable
    }

    class Time(input: Long) {
        var minutes: Int = (input / SECONDS_IN_MINUTE).toInt()
        var seconds: Int = (input % SECONDS_IN_MINUTE).toInt()
    }
}