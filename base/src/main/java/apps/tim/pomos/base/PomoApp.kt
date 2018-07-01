package apps.tim.pomos.base

import android.app.Application
import android.support.v4.content.ContextCompat
import apps.tim.pomos.base.PreferenceHelper.set
import apps.tim.pomos.base.di.app.AppComponent
import apps.tim.pomos.base.di.app.AppModule
import apps.tim.pomos.base.di.app.DaggerAppComponent
import apps.tim.pomos.base.di.data.DataModule
import apps.tim.pomos.base.ui.*

class PomoApp: Application() {

    companion object {
        lateinit var component: AppComponent
        lateinit var instance: PomoApp

        fun color(id: Int) = ContextCompat.getColor(instance, id)
        fun drawable(id: Int) = instance.resources.getDrawable(id)
        fun string(id: Int) = instance.resources.getString(id)
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .dataModule(DataModule())
                .build()
        setPrefs()
    }

    private fun setPrefs() {
        val work = PreferenceHelper.getWorkDuration(this)
        val rest = PreferenceHelper.getRestDuration(this)
        val daily = PreferenceHelper.getDaily(this)

        val sp = PreferenceHelper.defaultPrefs(this)
        if (work == DEFAULT_INT)
            sp[PREF_WORK_KEY] = WORK_DURATION_IN_MINUTE
        if (rest == DEFAULT_INT)
            sp[PREF_REST_KEY] = REST_DURATION_IN_MINUTE
        if (daily == DEFAULT_INT)
            sp[PREF_DAILY_KEY] = DAILY_GOAL
    }


}