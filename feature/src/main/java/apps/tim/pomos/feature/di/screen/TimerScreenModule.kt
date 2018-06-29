package apps.tim.pomos.feature.di.screen

import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import apps.tim.pomos.feature.ui.timer.Timer
import apps.tim.pomos.feature.ui.timer.TimerViewModel
import dagger.Module
import dagger.Provides


@Module
class TimerScreenModule {

    @Provides
    @TimerScope
    fun providesTimer() : Timer {
        return Timer()
    }

    @Provides
    @TimerScope
    fun providesTimerViewModel(tasksRepository: TasksRepository, timer: Timer): TimerViewModel {
        return TimerViewModel(tasksRepository, timer)
    }
}