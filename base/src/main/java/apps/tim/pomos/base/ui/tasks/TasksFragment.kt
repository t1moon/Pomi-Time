package apps.tim.pomos.base.ui.tasks

import android.app.Activity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import apps.tim.pomos.base.*
import apps.tim.pomos.base.ui.BACKLOG_FRAGMENT_PAGE
import apps.tim.pomos.base.ui.TODAY_FRAGMENT_PAGE
import apps.tim.pomos.base.ui.base.BaseFragment
import apps.tim.pomos.base.ui.picker.AddTaskFragment
import apps.tim.pomos.base.ui.tasks.pager.TaskListFragment
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_tasks_tabs.*
import java.util.concurrent.TimeUnit


class TasksFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks_tabs, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(taskToolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setupViewPager()
        checkForInitialShowCase()
        addTaskButton.setOnClickListener {
            val picker = AddTaskFragment()
            picker.show(this@TasksFragment.activity?.fragmentManager, "Picker")
        }
        finishBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_tasksFragment_to_statsFragment)
        }
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

    private fun checkShowcaseForBacklog() {
        val showcasePreference = ShowcasePreference(PreferenceHelper.defaultPrefs(PomoApp.instance))
        if (showcasePreference.backlogShowcaseShown)
            return
        add(Observable.just(Unit)
                .delay(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val transferBtn = viewPager.getChildAt(BACKLOG_FRAGMENT_PAGE).findViewById<View>(R.id.transferButton)
                    ShowcaseHelper.getTargetView(activity as Activity, transferBtn, ShowcaseHelper.Type.TRANSFER)
                    showcasePreference.backlogShowcaseShown = true
                })
    }

    private fun checkForInitialShowCase() {
        val showcasePreference = ShowcasePreference(PreferenceHelper.defaultPrefs(PomoApp.instance))
        if (showcasePreference.todayShowcaseShown)
            return

        val tab1 = (tabs.getChildAt(0) as ViewGroup).getChildAt(0)
        val tab2 = (tabs.getChildAt(0) as ViewGroup).getChildAt(1)
        TapTargetSequence(activity)
                .targets(
                        ShowcaseHelper.getTarget(tab1, ShowcaseHelper.Type.TODAY),
                        ShowcaseHelper.getTarget(tab2, ShowcaseHelper.Type.BACKLOG),
                        ShowcaseHelper.getTarget(finishBtn, ShowcaseHelper.Type.FINISH))
                .continueOnCancel(true)
                .listener(object : TapTargetSequence.Listener {
                    override fun onSequenceCanceled(lastTarget: TapTarget?) {}
                    override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}

                    override fun onSequenceFinish() {
                        val pomo = viewPager.getChildAt(TODAY_FRAGMENT_PAGE).findViewById<View>(R.id.taskDeadlineIcon)
                        ShowcaseHelper.getTargetView(activity as Activity, pomo, ShowcaseHelper.Type.POMOS)
                        showcasePreference.todayShowcaseShown = true
                    }
                })
                .start()
    }
}

