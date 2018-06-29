package apps.tim.pomos.feature.di.app

import apps.tim.pomos.feature.di.data.DataModule
import apps.tim.pomos.feature.di.fragment.FragmentComponent
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class, DataModule::class])
@Singleton
interface AppComponent {
    fun getFragmentComponent() : FragmentComponent
}
