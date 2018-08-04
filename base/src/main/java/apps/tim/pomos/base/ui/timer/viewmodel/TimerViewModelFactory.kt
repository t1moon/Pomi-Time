package apps.tim.pomos.base.ui.timer.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import apps.tim.pomos.base.data.repository.TasksRepository
import apps.tim.pomos.base.ui.timer.Timer
import apps.tim.pomos.base.ui.navigation.AppRouter
import javax.inject.Inject

class TimerViewModelFactory @Inject constructor(
        private val tasksRepository: TasksRepository,
        val timer: Timer,
        private val router: AppRouter) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(tasksRepository, timer, router) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}