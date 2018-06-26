package apps.tim.pomos.feature.ui.tasks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.base.BaseFragment
import apps.tim.pomos.feature.ui.picker.AddTaskFragment
import apps.tim.pomos.feature.ui.tasks.pager.TaskListFragment
import kotlinx.android.synthetic.main.fragment_tasks_tabs.*


class TasksFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks_tabs, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(taskToolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        setupViewPager()
        setupAddButton()
        setupFinishButton()
    }

    private fun setupFinishButton() {
        finishBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_tasksFragment_to_statsFragment)
        }
    }

    private fun setupAddButton() {
        addTaskButton.setOnClickListener {
            val picker = AddTaskFragment()
            picker.show(this@TasksFragment.activity?.fragmentManager, "Picker")
        }
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        for (i in 0..1)
            adapter.addFragment(TaskListFragment.newInstance(i))
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }
}

