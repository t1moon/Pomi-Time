package apps.tim.pomos.base.ui.timer.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import apps.tim.pomos.base.data.repository.TasksRepository
import apps.tim.pomos.base.ui.timer.Timer
import javax.inject.Inject

class TimerViewModelFactory @Inject constructor(val tasksRepository: TasksRepository, val timer: Timer) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(tasksRepository, timer) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}