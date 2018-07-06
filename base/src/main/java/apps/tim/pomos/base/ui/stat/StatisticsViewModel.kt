package apps.tim.pomos.base.ui.stat

import apps.tim.pomos.base.PomoApp
import apps.tim.pomos.base.PreferenceHelper
import apps.tim.pomos.base.data.Statistics
import apps.tim.pomos.base.data.TasksRepository
import apps.tim.pomos.base.ui.STATISTIC_COUNT
import apps.tim.pomos.base.ui.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

class StatisticsViewModel(private val tasksRepository: TasksRepository) : BaseViewModel() {

    val statisticsItemsObservable: PublishSubject<List<StatisticsItem>> = PublishSubject.create()
    val statisticsObservable: PublishSubject<List<Statistics>> = PublishSubject.create()
    val finishedObservable: PublishSubject<Unit> = PublishSubject.create()

    fun loadStatistics() {
        getStatisticsItemsForToday().subscribe { statisticsItemsObservable.onNext(it) }.addTo(composite)
        getStatisticsList().subscribe { statisticsObservable.onNext(it) }.addTo(composite)
    }

    fun getStatisticsList(): Flowable<List<Statistics>> {
        return tasksRepository.getStats()
                .flatMap {
                    Flowable.fromIterable(it)
                            .sorted { o1, o2 ->
                                o2.date.compareTo(o1.date)
                            }
                            .take(STATISTIC_COUNT.toLong())
                            .toList()
                            .toFlowable()
                }
    }

    fun getStatisticsItemsForToday(): Flowable<List<StatisticsItem>> {
        val daily = PreferenceHelper.getDaily(PomoApp.instance)
        return tasksRepository.getTasks()
                .flatMap {
                    Flowable.fromIterable(it)
                            .filter {
                                it.isActive
                            }
                            .sorted { task1, task2 ->
                                task1.isComplete.compareTo(task2.isComplete)
                            }
                            .map {
                                StatisticsItem(
                                        it.title,
                                        it.currentPomo,
                                        it.deadline,
                                        it.isComplete,
                                        (it.currentPomo / daily.toFloat() * 100).toInt()
                                )
                            }
                            .toList()
                            .toFlowable()
                }
    }

    fun finishSession(stat: Statistics) {
        tasksRepository.deleteCompletedTasks()
                .andThen( tasksRepository.moveActiveTasksToBacklog())
                .andThen( tasksRepository.addStatistics(stat))
                .subscribe {
                    finishedObservable.onNext(Unit)
                }.addTo(composite)
    }

}