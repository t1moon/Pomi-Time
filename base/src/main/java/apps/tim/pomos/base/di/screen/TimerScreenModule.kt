package apps.tim.pomos.base.di.screen

import apps.tim.pomos.base.ui.tasks.data.TasksRepository
import apps.tim.pomos.base.ui.timer.Timer
import apps.tim.pomos.base.ui.timer.TimerViewModel
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