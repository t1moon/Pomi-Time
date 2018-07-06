package apps.tim.pomos.base.di.fragment

import apps.tim.pomos.base.ui.tasks.TasksViewModel
import apps.tim.pomos.base.data.TasksRepository
import dagger.Module
import dagger.Provides


@Module
class FragmentModule {

    @Provides
    @FragmentScope
    fun providesTasksViewModel(tasksRepository: TasksRepository): TasksViewModel {
        return TasksViewModel(tasksRepository)
    }
}