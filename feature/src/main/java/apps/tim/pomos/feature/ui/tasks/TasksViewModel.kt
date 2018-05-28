package apps.tim.pomos.feature.ui.tasks

import android.arch.lifecycle.ViewModel
import apps.tim.pomos.feature.ui.tasks.data.Task
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import io.reactivex.Flowable


class TasksViewModel(val tasksRepository: TasksRepository) : ViewModel() {


    fun getTasks(pos: Int?): Flowable<List<Task>> {
        return tasksRepository.getTasks(pos)
    }

}