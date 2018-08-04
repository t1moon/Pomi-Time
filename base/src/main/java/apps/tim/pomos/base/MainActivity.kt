package apps.tim.pomos.base

import android.os.Bundle
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.ui.base.BaseActivity
import apps.tim.pomos.base.ui.navigation.AppRouter
import apps.tim.pomos.base.ui.navigation.MainNavigator
import apps.tim.pomos.base.ui.navigation.Screens
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: AppRouter

    private val navigator = MainNavigator(this, R.id.fragmentContainer)

    override fun getNavigator(): Navigator = navigator


    override fun getNavigationHolder(): NavigatorHolder = navigatorHolder

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun provideInjection() = PomoApp.component.inject(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            router.newRootScreen(Screens.TASKS_SCREEN)
    }
}
