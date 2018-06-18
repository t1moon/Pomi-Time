package apps.tim.pomos.feature.di.fragment

import apps.tim.pomos.feature.ui.picker.AddTaskFragment
import apps.tim.pomos.feature.ui.picker.EditTaskFragment
import apps.tim.pomos.feature.ui.tasks.pager.TaskListFragment
import apps.tim.pomos.feature.ui.timer.TimerFragment
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [ViewModelModule::class])
interface FragmentComponent {
    fun inject(fragment: TaskListFragment)
    fun inject(fragment: AddTaskFragment)
    fun inject(fragment: EditTaskFragment)
    fun inject(fragment: TimerFragment)
}