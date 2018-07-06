package apps.tim.pomos.base.ui.stat.viewmodel

import apps.tim.pomos.base.data.entity.Statistics
import apps.tim.pomos.base.data.repository.TasksRepository
import apps.tim.pomos.base.preference.SettingsPreference
import apps.tim.pomos.base.ui.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import java.util.*

class StatisticsViewModel(private val tasksRepository: TasksRepository) : BaseViewModel() {

    companion object {
        const val STATISTIC_COUNT = 3
    }

    val finishedObservable: PublishSubject<Unit> = PublishSubject.create()
    val statsReadyObservable: PublishSubject<CombinedStatistics> = PublishSubject.create()

    fun loadStatistics() {
        val daily = SettingsPreference.dailyGoal
        var total = 0

        val items = getStatisticsItemsForToday()
                .doOnNext {
                    total = (it.fold(0)
                    { total, next: StatisticsListItem -> total + next.pomo } / daily.toFloat() * 100).toInt()
                }

        val currentStat = Statistics(
                id = 0,
                date = Calendar.getInstance().timeInMillis,
                completed = total
        )

        val stats = getStatisticsList()
                .flatMap {
                    val list = mutableListOf<Statistics>()
                    list.addAll(it)
                    list.add(0, currentStat)
                    Flowable.fromIterable(list)
                            .toList()
                            .toFlowable()
                }

        Flowable.combineLatest(items, stats, BiFunction(StatisticsViewModel::CombinedStatistics))
                .subscribe {
                    statsReadyObservable.onNext(it)
                }
                .addTo(composite)
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

    fun getStatisticsItemsForToday(): Flowable<List<StatisticsListItem>> {
        val daily = SettingsPreference.dailyGoal
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
                                StatisticsListItem(
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
                .andThen(tasksRepository.moveActiveTasksToBacklog())
                .andThen(tasksRepository.addStatistics(stat))
                .subscribe {
                    finishedObservable.onNext(Unit)
                }.addTo(composite)
    }

    data class CombinedStatistics(val items: List<StatisticsListItem>, val stats: List<Statistics>)
}