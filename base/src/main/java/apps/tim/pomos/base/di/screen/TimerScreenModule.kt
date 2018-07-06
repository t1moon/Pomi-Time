package apps.tim.pomos.base.di.screen

import apps.tim.pomos.base.TimerViewModelFactory
import apps.tim.pomos.base.data.TasksRepository
import apps.tim.pomos.base.ui.timer.Timer
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
    fun provideTimerViewModelFactory(tasksRepository: TasksRepository, timer: Timer) : TimerViewModelFactory {
        return TimerViewModelFactory(tasksRepository, timer)
    }

}