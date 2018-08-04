package apps.tim.pomos.base.ui.navigation

import android.content.Context
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import apps.tim.pomos.base.R
import apps.tim.pomos.base.data.entity.Task
import apps.tim.pomos.base.ui.settings.SettingsFragment
import apps.tim.pomos.base.ui.stat.StatisticsFragment
import apps.tim.pomos.base.ui.tasks.TasksFragment
import apps.tim.pomos.base.ui.timer.TimerFragment
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

class MainNavigator(activity: FragmentActivity, @IdRes container: Int)
    : SupportAppNavigator(activity, container) {

    override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? =
            null

    override fun createFragment(screenKey: String?, data: Any?): Fragment? =
            when (screenKey) {
                Screens.STATISTICS_SCREEN -> StatisticsFragment.newInstance()
                Screens.TIMER_SCREEN -> TimerFragment.newInstance(data as Task)
                Screens.SETTINGS_SCREEN -> SettingsFragment.newInstance()
                Screens.TASKS_SCREEN -> TasksFragment.newInstance()
                else -> null
            }

    override fun setupFragmentTransactionAnimation(command: Command,
                                                   currentFragment: Fragment?,
                                                   nextFragment: Fragment,
                                                   fragmentTransaction: FragmentTransaction) {
        fragmentTransaction.setCustomAnimations(
                R.anim.nav_enter,
                R.anim.nav_back,
                R.anim.nav_pop_enter,
                R.anim.nav_pop_back
        )
    }

}