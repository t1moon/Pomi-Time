package apps.tim.pomos.feature.di.data

import android.arch.persistence.room.Room
import android.content.Context
import apps.tim.pomos.feature.ui.TASK_DB
import apps.tim.pomos.feature.ui.tasks.data.TaskDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun providesRoomDatabase(context: Context) =
            Room.databaseBuilder(context, TaskDatabase::class.java, TASK_DB).build()
}
