package apps.tim.pomos.base.ui.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder

abstract class BaseActivity : AppCompatActivity() {
    protected abstract fun getNavigator(): Navigator
    protected abstract fun getNavigationHolder(): NavigatorHolder

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        provideInjection()
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        getNavigationHolder().setNavigator(getNavigator())
    }

    @CallSuper
    override fun onPause() {
        getNavigationHolder().removeNavigator()
        super.onPause()
    }

    abstract fun getLayoutId() : Int
    abstract fun provideInjection()

}