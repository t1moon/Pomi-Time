package apps.tim.pomos.base.di.fragment

import apps.tim.pomos.base.di.screen.TimerScreenComponent
import apps.tim.pomos.base.ui.picker.AddTaskFragment
import apps.tim.pomos.base.ui.picker.EditTaskFragment
import apps.tim.pomos.base.ui.stat.StatisticsFragment
import apps.tim.pomos.base.ui.tasks.pager.TaskListFragment
import dagger.Subcomponent


@Subcomponent(modules = [FragmentModule::class])
@FragmentScope
interface FragmentComponent {
    fun inject(fragment: TaskListFragment)
    fun inject(fragment: AddTaskFragment)
    fun inject(fragment: EditTaskFragment)
    fun inject(fragment: StatisticsFragment)

    fun getTimerScreenComponent(): TimerScreenComponent
}