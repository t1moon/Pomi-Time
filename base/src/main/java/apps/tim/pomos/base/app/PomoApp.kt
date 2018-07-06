package apps.tim.pomos.base.app

import android.app.Application
import android.support.v4.content.ContextCompat
import apps.tim.pomos.base.di.app.AppComponent
import apps.tim.pomos.base.di.app.AppModule
import apps.tim.pomos.base.di.app.DaggerAppComponent
import apps.tim.pomos.base.di.data.DataModule

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
    }

}