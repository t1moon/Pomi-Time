package apps.tim.pomos.feature

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.support.v4.content.ContextCompat
import apps.tim.pomos.feature.ui.tasks.data.TaskDatabase

class PomoApp: Application() {
    init {
        instance = this
    }

    companion object {
        var instance: PomoApp? = null
        var database: TaskDatabase? = null


        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        PomoApp.database =  Room.databaseBuilder(this, TaskDatabase::class.java, "tasks_db").build()
    }

    fun color(id: Int) = ContextCompat.getColor(instance!!.applicationContext, id)
    fun drawable(id: Int) = resources.getDrawable(id)
}