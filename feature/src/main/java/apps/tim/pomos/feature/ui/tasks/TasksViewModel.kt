package apps.tim.pomos.feature.ui.tasks

import apps.tim.pomos.feature.ui.DAILY_GOAL
import apps.tim.pomos.feature.ui.STATISTIC_COUNT
import apps.tim.pomos.feature.ui.stat.StatisticsItem
import apps.tim.pomos.feature.ui.tasks.data.Statistics
import apps.tim.pomos.feature.ui.tasks.data.Task
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import io.reactivex.Flowable


class TasksViewModel(private val tasksRepository: TasksRepository) {

    fun addTask(task: Task) {
        tasksRepository.addTask(task)
    }

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
        return getTodayTasks()
                .flatMap {
                    Flowable.fromIterable(it)
                            .map {
                                StatisticsItem(
                                        it.title,
                                        it.currentPomo,
                                        it.deadline,
                                        it.isComplete,
                                        (it.currentPomo / DAILY_GOAL.toFloat() * 100).toInt()
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

    fun updateTitleById(title: String, id: Long) {
        tasksRepository.updateTitle(title, id)
    }

    fun deleteTask(task: Task) {
        tasksRepository.deleteTask(task)
    }

    fun markIsCompleteTaskById(complete: Boolean, id: Long) {
        tasksRepository.completeTaskById(complete, id)
    }

    fun activateTask(id: Long) {
        tasksRepository.activateTask(id)
    }

    fun finishSession(stat: Statistics) {
        tasksRepository.deleteCompletedTasks()
        tasksRepository.moveActiveTasksToBacklog()
        tasksRepository.addStatistics(stat)
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