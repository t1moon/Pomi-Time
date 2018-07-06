package apps.tim.pomos.base.ui.tasks

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import apps.tim.pomos.base.BACKLOG_FRAGMENT_PAGE
import apps.tim.pomos.base.R
import apps.tim.pomos.base.TODAY_FRAGMENT_PAGE
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.ui.tasks.viewpager.TaskListFragment

class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    private val fragmentList = mutableListOf<TaskListFragment>()

    fun addFragment(fragment: TaskListFragment) = fragmentList.add(fragment)

    override fun getItem(position: Int) = fragmentList[position]

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            TODAY_FRAGMENT_PAGE -> return PomoApp.string(R.string.task_tabs_1)
            BACKLOG_FRAGMENT_PAGE -> return PomoApp.string(R.string.task_tabs_2)
        }
        return null
    }

    override fun getCount() = fragmentList.size

}