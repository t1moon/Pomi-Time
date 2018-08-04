package apps.tim.pomos.base.di.screen

import apps.tim.pomos.base.data.repository.TasksRepository
import apps.tim.pomos.base.ui.timer.Timer
import apps.tim.pomos.base.ui.timer.viewmodel.TimerViewModelFactory
import apps.tim.pomos.base.ui.navigation.AppRouter
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
    fun provideTimerViewModelFactory(tasksRepository: TasksRepository, timer: Timer, router: AppRouter) : TimerViewModelFactory {
        return TimerViewModelFactory(tasksRepository, timer, router)
    }

}