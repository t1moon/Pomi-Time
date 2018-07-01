package apps.tim.pomos.base.di.screen

import apps.tim.pomos.base.ui.timer.TimerFragment
import dagger.Subcomponent


@Subcomponent(modules = [TimerScreenModule::class])
@TimerScope
interface TimerScreenComponent {
    fun inject(fragment: TimerFragment)
}