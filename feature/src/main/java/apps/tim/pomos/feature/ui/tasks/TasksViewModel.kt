package apps.tim.pomos.feature.ui.tasks

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
                                !it.complete
                                it.isActive
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
                                !it.complete
                                !it.isActive
                            }
                            .toList()
                            .toFlowable()
                }
    }

    fun updateTask(title: String, id: Long) {
        tasksRepository.updateTitle(title, id)
    }

    fun deleteTask(task: Task) {
        tasksRepository.deleteTask(task)
    }

    fun completeTaskById(complete: Boolean, id: Long) {
        tasksRepository.completeTaskById(complete, id)
    }

    fun activateTask(id: Long) {
        tasksRepository.activateTask(id)
    }
}