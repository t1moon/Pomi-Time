package apps.tim.pomos.feature

import android.app.Application
import android.support.v4.content.ContextCompat
import apps.tim.pomos.feature.di.app.AppComponent
import apps.tim.pomos.feature.di.app.AppModule
import apps.tim.pomos.feature.di.app.DaggerAppComponent
import apps.tim.pomos.feature.di.data.DataModule
import apps.tim.pomos.feature.di.data.RoomModule

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
                .roomModule(RoomModule())
                .dataModule(DataModule())
                .build()
        component.inject(this)
    }


}