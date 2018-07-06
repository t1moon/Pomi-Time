package apps.tim.pomos.base.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import apps.tim.pomos.base.data.dao.StatisticsDao
import apps.tim.pomos.base.data.dao.TaskDao
import apps.tim.pomos.base.data.entity.Statistics
import apps.tim.pomos.base.data.entity.Task

@Database(entities = [(Task::class), (Statistics::class)], version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun statDao(): StatisticsDao
}