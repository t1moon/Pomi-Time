package apps.tim.pomos.feature

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import apps.tim.pomos.feature.ui.tasks.data.TaskDatabase

class PomoApp: Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: PomoApp? = null
        var database: TaskDatabase? = null


        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        PomoApp.database =  Room.databaseBuilder(this, TaskDatabase::class.java, "tasks_db").build()
    }
}