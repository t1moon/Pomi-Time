package apps.tim.pomos.feature.di.fragment

import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
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