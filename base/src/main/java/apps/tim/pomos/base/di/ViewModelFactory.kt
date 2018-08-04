package apps.tim.pomos.base.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import apps.tim.pomos.base.data.repository.TasksRepository
import apps.tim.pomos.base.ui.stat.viewmodel.StatisticsViewModel
import apps.tim.pomos.base.ui.tasks.TasksViewModel
import apps.tim.pomos.base.ui.navigation.AppRouter
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
        val tasksRepository: TasksRepository,
        val router: AppRouter) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(tasksRepository, router) as T
        }
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            return StatisticsViewModel(tasksRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



