package apps.tim.pomos.feature.ui.tasks

import android.arch.lifecycle.ViewModel
import apps.tim.pomos.feature.ui.tasks.data.Task
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import io.reactivex.Flowable




class TasksViewModel(private val tasksRepository: TasksRepository) : ViewModel() {


    fun addTask() {
        val task = Task(null, "Some task")
        tasksRepository.addTask(task)
    }
    fun getTasks(pos: Int?): Flowable<List<Task>>? {
        return tasksRepository.getTasks(pos)
    }

}