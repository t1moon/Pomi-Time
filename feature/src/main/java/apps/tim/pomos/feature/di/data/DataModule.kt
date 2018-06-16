package apps.tim.pomos.feature.di.data

import apps.tim.pomos.feature.ui.tasks.data.TaskDatabase
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataModule {

    @Provides
    @Singleton
    fun providesTasksRepository(taskDatabase: TaskDatabase) : TasksRepository {
        return TasksRepository(taskDatabase)
    }
}
