package apps.tim.pomos.base.di.navigation

import apps.tim.pomos.base.ui.navigation.AppRouter
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder

@Module
class NavigationModule {
    private val cicerone = Cicerone.create(AppRouter())

    @Provides
    fun providesRouter() : AppRouter
            = cicerone.router

    @Provides
    fun providesNavigationHelper() : NavigatorHolder
            = cicerone.navigatorHolder
}