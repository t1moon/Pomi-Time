package apps.tim.pomos.feature.di.app

import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.di.data.DataModule
import apps.tim.pomos.feature.di.data.RoomModule
import apps.tim.pomos.feature.di.fragment.FragmentComponent
import apps.tim.pomos.feature.di.fragment.ViewModelModule
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class, RoomModule::class, DataModule::class])
@Singleton
interface AppComponent {
    fun inject(app: PomoApp)
    fun getFragmentComponent() : FragmentComponent
}
