package apps.tim.pomos.feature

import android.app.Application
import android.content.Context

class PomoApp: Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: PomoApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = PomoApp.applicationContext()
    }
}