package apps.tim.pomos.feature.ui.tasks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.tasks.pager.PagerAdapter
import apps.tim.pomos.feature.ui.tasks.pager.TaskListFragment
import kotlinx.android.synthetic.main.fragment_tasks_tabs.*

class TasksFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(taskToolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = PagerAdapter(childFragmentManager)
        for (i in 1..4)
            adapter.addFragment(TaskListFragment())
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) = adapter.loadData(position)
        })
        tabs.setupWithViewPager(viewPager)
        adapter.loadData(0)
    }
}