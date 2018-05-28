package apps.tim.pomos.feature.ui.tasks

import android.arch.lifecycle.ViewModel
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.ui.tasks.data.Task
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TasksViewModel(val tasksRepository: TasksRepository) : ViewModel() {


    fun addTask() {
        val task = Task(null, "Some task")
        tasksRepository.addTask(task)
    }
    fun getTasks(pos: Int?): Flowable<List<Task>>? {
        return tasksRepository.getTasks(pos)
    }

}