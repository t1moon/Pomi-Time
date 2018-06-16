package apps.tim.pomos.feature.ui.tasks

import android.arch.lifecycle.ViewModel
import apps.tim.pomos.feature.ui.tasks.data.Task
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import io.reactivex.Flowable


class TasksViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    fun addTask(text: String) {
        val task = Task(0, text)
        tasksRepository.addTask(task)
    }

    fun getTasks(pos: Int?): Flowable<List<Task>>? {
        return tasksRepository.getTasks(pos)
                ?.flatMap {
                    Flowable.fromIterable(it)
                            .filter {
                                !it.complete
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
}