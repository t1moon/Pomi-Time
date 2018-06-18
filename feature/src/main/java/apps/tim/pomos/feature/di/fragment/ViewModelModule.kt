package apps.tim.pomos.feature.di.fragment

import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import apps.tim.pomos.feature.ui.timer.Timer
import apps.tim.pomos.feature.ui.timer.TimerViewModel
import dagger.Module
import dagger.Provides


@Module
class ViewModelModule {

    @Provides
    @FragmentScope
    fun providesTasksViewModel(tasksRepository: TasksRepository): TasksViewModel {
        return TasksViewModel(tasksRepository)
    }

    @Provides
    @FragmentScope
    fun providesTimer() : Timer {
        return Timer()
    }

    @Provides
    @FragmentScope
    fun providesTimerViewModel(timer: Timer): TimerViewModel {
        return TimerViewModel(timer)
    }
}