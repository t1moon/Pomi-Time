package apps.tim.pomos.feature.ui.tasks

import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.PreferenceHelper
import apps.tim.pomos.feature.ui.STATISTIC_COUNT
import apps.tim.pomos.feature.ui.stat.StatisticsItem
import apps.tim.pomos.feature.ui.tasks.data.Statistics
import apps.tim.pomos.feature.ui.tasks.data.Task
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function3


class TasksViewModel(private val tasksRepository: TasksRepository) {

    fun addTask(task: Task) =
            tasksRepository.addTask(task)

    fun getTodayTasks(): Flowable<List<Task>> {
        return tasksRepository.getTasks()
                .flatMap {
                    Flowable.fromIterable(it)
                            .filter {
                                it.isActive
                            }
                            .sorted { task1, task2 ->
                                task1.isComplete.compareTo(task2.isComplete)
                            }
                            .toList()
                            .toFlowable()
                }
    }

    fun getStatisticsForToday(): Flowable<List<StatisticsItem>> {
        val daily = PreferenceHelper.getDaily(PomoApp.instance)
        return getTodayTasks()
                .flatMap {
                    Flowable.fromIterable(it)
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

    fun getBacklogTasks(): Flowable<List<Task>> {
        return tasksRepository.getTasks()
                .flatMap {
                    Flowable.fromIterable(it)
                            .filter {
                                !it.isComplete
                                !it.isActive
                            }
                            .toList()
                            .toFlowable()
                }
    }


    fun deleteTask(task: Task) =
        tasksRepository.deleteTask(task)

    fun markIsCompleteTaskById(complete: Boolean, id: Long) =
        tasksRepository.completeTaskById(complete, id)


    fun activateTask(id: Long) =
        tasksRepository.activateTask(id)


    fun finishSession(stat: Statistics) : Single<Unit> {
        println(stat.date)
        return Single.zip(
                tasksRepository.deleteCompletedTasks(),
        tasksRepository.moveActiveTasksToBacklog(),
        tasksRepository.addStatistics(stat),
                Function3<Unit, Unit, Unit, Unit> {
                    _, _, _ ->
                    Single.just(Unit)
                })
    }

    fun getStats() : Flowable<List<Statistics>>{
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

}