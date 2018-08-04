package apps.tim.pomos.base.di.app

import apps.tim.pomos.base.MainActivity
import apps.tim.pomos.base.di.data.DataModule
import apps.tim.pomos.base.di.fragment.FragmentComponent
import apps.tim.pomos.base.di.navigation.NavigationModule
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class, DataModule::class, NavigationModule::class])
@Singleton
interface AppComponent {
    fun getFragmentComponent() : FragmentComponent
    fun inject(mainActivity: MainActivity)
}
