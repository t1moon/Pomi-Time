package apps.tim.pomos.feature.ui.tasks.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(Task::class), (Statistics::class)], version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun statDao(): StatisticsDao
}