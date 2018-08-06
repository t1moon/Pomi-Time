package apps.tim.pomos.base.ui.tasks

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.base.BACKLOG_FRAGMENT_PAGE
import apps.tim.pomos.base.R
import apps.tim.pomos.base.TODAY_FRAGMENT_PAGE
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.di.ViewModelFactory
import apps.tim.pomos.base.showcase.ShowcaseHelper
import apps.tim.pomos.base.ui.addtask.AddTaskFragment
import apps.tim.pomos.base.ui.base.BaseFragment
import apps.tim.pomos.base.ui.tasks.viewpager.TaskListFragment
import kotlinx.android.synthetic.main.fragment_tasks_tabs.*
import javax.inject.Inject


class TasksFragment : BaseFragment() {
    companion object {
        fun newInstance() = TasksFragment()
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var tasksViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
        tasksViewModel = ViewModelProviders.of(this, viewModelFactory)[TasksViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(taskToolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setupViewPager()
        setButtonListeners()
        showCase()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        for (i in 0..1)
            adapter.addFragment(TaskListFragment.newInstance(i))
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (position == BACKLOG_FRAGMENT_PAGE) {
                    checkShowcaseForBacklog()
                    viewPager.removeOnPageChangeListener(this)
                }
            }
        })
    }

    private fun setButtonListeners() {
        addTaskButton.setOnClickListener {
            val picker = AddTaskFragment()
            picker.show(activity?.supportFragmentManager, "AddTask")
        }
        finishBtn.setOnClickListener {
            tasksViewModel.openStats()
        }
    }

    private fun checkShowcaseForBacklog() {
        Handler().postDelayed({
            val transferBtn = viewPager.getChildAt(BACKLOG_FRAGMENT_PAGE).findViewById<View>(R.id.transferButton)
            ShowcaseHelper.showBacklogPageShowcase(activity, transferBtn)
        }, 200)

    }

    private fun showCase() {
        val tab1 = (tabs.getChildAt(0) as ViewGroup).getChildAt(0)
        val tab2 = (tabs.getChildAt(0) as ViewGroup).getChildAt(1)
        ShowcaseHelper.showTasksFragmentShowcase(activity, this::getPomoView, tab1, tab2, finishBtn)
    }

    // It is necessary to return pomoView as function,
    // because pomoView won't be null by the time it is requested
    private fun getPomoView() = viewPager.getChildAt(TODAY_FRAGMENT_PAGE).findViewById<View>(R.id.taskDeadlineIcon) as View
}

