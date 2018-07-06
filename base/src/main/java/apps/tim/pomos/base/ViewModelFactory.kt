package apps.tim.pomos.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import apps.tim.pomos.base.data.TasksRepository
import apps.tim.pomos.base.ui.stat.StatisticsViewModel
import apps.tim.pomos.base.ui.tasks.TasksViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(val tasksRepository: TasksRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(tasksRepository) as T
        }
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            return StatisticsViewModel(tasksRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}