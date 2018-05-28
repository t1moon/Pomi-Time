package apps.tim.pomos.feature.ui.tasks.pager

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R

class PagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    private val fragmentList = mutableListOf<TaskListFragment>()

    fun addFragment(fragment: TaskListFragment) = fragmentList.add(fragment)

    override fun getItem(position: Int) = fragmentList[position]

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> return PomoApp.applicationContext().getString(R.string.task_tabs_1)
            1 -> return PomoApp.applicationContext().getString(R.string.task_tabs_2)
            2 -> return PomoApp.applicationContext().getString(R.string.task_tabs_3)
            3 -> return PomoApp.applicationContext().getString(R.string.task_tabs_4)
        }
        return null
    }

    override fun getCount() = fragmentList.size
    fun loadData(position: Int) {
        fragmentList[position].loadTasks(position)
    }

}