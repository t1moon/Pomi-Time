package apps.tim.pomos.base.di.app

import apps.tim.pomos.base.di.data.DataModule
import apps.tim.pomos.base.di.fragment.FragmentComponent
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class, DataModule::class])
@Singleton
interface AppComponent {
    fun getFragmentComponent() : FragmentComponent
}
