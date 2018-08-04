package apps.tim.pomos.base.ui.tasks

import apps.tim.pomos.base.DEFAULT_DATE_LONG
import apps.tim.pomos.base.data.entity.Statistics
import apps.tim.pomos.base.data.entity.Task
import apps.tim.pomos.base.data.repository.TasksRepository
import apps.tim.pomos.base.ui.base.BaseViewModel
import apps.tim.pomos.base.ui.navigation.AppRouter
import apps.tim.pomos.base.ui.navigation.Screens
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.toSingle
import io.reactivex.subjects.PublishSubject


class TasksViewModel(
        private val tasksRepository: TasksRepository,
        private val router: AppRouter) : BaseViewModel() {

    val todayListObservable: PublishSubject<List<Task>> = PublishSubject.create()
    val backlogListObservable: PublishSubject<List<Task>> = PublishSubject.create()

    fun loadTasks() {
        getTodayTasks().subscribe { todayListObservable.onNext(it) }.addTo(composite)
        getBacklogTasks().subscribe { backlogListObservable.onNext(it) }.addTo(composite)
    }

    fun addTask(task: Task) =
            tasksRepository.addTask(task)

    private fun getTodayTasks(): Flowable<List<Task>> {
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

    private fun getBacklogTasks(): Flowable<List<Task>> {
        return tasksRepository.getTasks()
                .flatMap {
                    Flowable.fromIterable(it)
                            .filter {
                                !it.isComplete
                                !it.isActive
                            }
                            .sorted { o1, o2 ->
                                if (o1.deadline != DEFAULT_DATE_LONG && o2.deadline == DEFAULT_DATE_LONG)
                                    return@sorted -1
                                if (o1.deadline == DEFAULT_DATE_LONG && o2.deadline != DEFAULT_DATE_LONG)
                                    return@sorted 1
                                if (o1.deadline == DEFAULT_DATE_LONG && o2.deadline == DEFAULT_DATE_LONG)
                                    return@sorted o2.created.compareTo(o1.created)

                                o1.deadline.compareTo(o2.deadline)
                            }
                            .toList()
                            .toFlowable()
                }
    }

    fun deleteTask(task: Task) =
            tasksRepository.deleteTask(task)

    fun markIsCompleteTaskById(complete: Boolean, id: Long) =
            tasksRepository.setCompleteTaskById(complete, id)

    fun activateTask(id: Long) =
            tasksRepository.activateTask(id)

    fun finishSession(stat: Statistics): Completable {
        return Single.zip(
                tasksRepository.deleteCompletedTasks().toSingle(),
                tasksRepository.moveActiveTasksToBacklog().toSingle(),
                tasksRepository.addStatistics(stat).toSingle(),
                Function3<Completable, Completable, Completable, Completable> { _, _, _ ->
                    Completable.complete()
                })
                .toCompletable()
    }

    fun openTimer(task: Task) {
        router.navigateTo(Screens.TIMER_SCREEN, task)
    }

    fun openStats() {
        router.navigateTo(Screens.STATISTICS_SCREEN)
    }
}