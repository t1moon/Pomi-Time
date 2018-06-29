package apps.tim.pomos.feature.di.screen

import apps.tim.pomos.feature.ui.timer.TimerFragment
import dagger.Subcomponent


@Subcomponent(modules = [TimerScreenModule::class])
@TimerScope
interface TimerScreenComponent {
    fun inject(fragment: TimerFragment)
}