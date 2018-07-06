package apps.tim.pomos.base.di.data

import android.arch.persistence.room.Room
import android.content.Context
import apps.tim.pomos.base.data.TaskDatabase
import apps.tim.pomos.base.data.TasksRepository
import apps.tim.pomos.base.ui.TASK_DB
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

    @Provides
    @Singleton
    fun providesRoomDatabase(context: Context) =
            Room.databaseBuilder(context, TaskDatabase::class.java, TASK_DB).build()

}
