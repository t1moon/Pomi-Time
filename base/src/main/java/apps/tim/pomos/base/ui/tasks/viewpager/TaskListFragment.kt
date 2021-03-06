package apps.tim.pomos.base.ui.tasks.viewpager

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.base.R
import apps.tim.pomos.base.TASK_ARG
import apps.tim.pomos.base.TODAY_FRAGMENT_PAGE
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.data.entity.Task
import apps.tim.pomos.base.di.ViewModelFactory
import apps.tim.pomos.base.showcase.ShowcaseHelper
import apps.tim.pomos.base.ui.base.BaseFragment
import apps.tim.pomos.base.ui.edittask.EditTaskFragment
import apps.tim.pomos.base.ui.tasks.TasksViewModel
import apps.tim.pomos.base.ui.tasks.viewpager.backlog.BacklogAdapter
import apps.tim.pomos.base.ui.tasks.viewpager.today.TodayTasksAdapter
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import javax.inject.Inject


class TaskListFragment : BaseFragment() {
    private var fragmentPage: Int = TODAY_FRAGMENT_PAGE
    private lateinit var todayAdapter: TodayTasksAdapter
    private lateinit var backlogAdapter: BacklogAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var tasksViewModel: TasksViewModel

    companion object {
        const val FRAGMENT_PAGE_KEY = "POSITION"

        fun newInstance(pos: Int): TaskListFragment {
            val fragment = TaskListFragment()
            val bundle = Bundle()
            bundle.putInt(FRAGMENT_PAGE_KEY, pos)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
        fragmentPage = arguments?.get(FRAGMENT_PAGE_KEY) as Int
        tasksViewModel = ViewModelProviders.of(this, viewModelFactory)[TasksViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_tasks_list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTaskList()
        setTodayAdapter()
        setBacklogAdapter()
        observeToData(fragmentPage)
        // Set initial showcase items just once
        if (fragmentPage == TODAY_FRAGMENT_PAGE)
            setInitialShowcaseItems()

        tasksViewModel.loadTasks()
    }

    private fun observeToData(fragmentPage: Int) {
        tasksViewModel.apply {
            if (fragmentPage == TODAY_FRAGMENT_PAGE)
                todayListObservable
                        .subscribe(this@TaskListFragment::updateTodayList, this@TaskListFragment::showError)
                        .addTo(compositeDisposable)
            else
                backlogListObservable
                        .subscribe(this@TaskListFragment::updateBacklogList, this@TaskListFragment::showError)
                        .addTo(compositeDisposable)
        }
    }

    private fun setTaskList() {
        taskList.layoutManager = LinearLayoutManager(context)
        taskList.itemAnimator = DefaultItemAnimator()
    }

    private fun setBacklogAdapter() {
        backlogAdapter = BacklogAdapter(
                ({
                    tasksViewModel.activateTask(it.id).subscribe().addTo(compositeDisposable)
                }),
                ({
                    val bundle = Bundle()
                    bundle.putParcelable(TASK_ARG, it)
                    val picker = EditTaskFragment()
                    picker.arguments = bundle
                    picker.show(activity?.supportFragmentManager, "")
                    true
                }))
    }

    private fun setTodayAdapter() {
        todayAdapter = TodayTasksAdapter(
                ({
                    tasksViewModel.openTimer(it)
                }),
                ({
                    val bundle = Bundle()
                    bundle.putParcelable(TASK_ARG, it)
                    val picker = EditTaskFragment()
                    picker.arguments = bundle
                    picker.show(activity?.supportFragmentManager, "")
                    true
                }),
                ({ task: Task, checked: Boolean ->
                    tasksViewModel.markIsCompleteTaskById(checked, task.id).subscribe().addTo(compositeDisposable)
                }))
    }

    private fun updateTodayList(list: List<Task>) {
        taskList.adapter = todayAdapter
        todayAdapter.submitList(list)
    }

    private fun updateBacklogList(list: List<Task>) {
        taskList.adapter = backlogAdapter
        backlogAdapter.submitList(list)
    }

    private fun setInitialShowcaseItems() {
        if (!ShowcaseHelper.isShowcaseItemsAdded()) {
            val todayExampleTask = ShowcaseHelper.getTodayExample()
            tasksViewModel
                    .addTask(todayExampleTask)
                    .subscribe(this::onInitialShowcaseItemsAdded, this::showError)
                    .addTo(compositeDisposable)

            val backlogExampleTask = ShowcaseHelper.getBacklogExample()
            tasksViewModel
                    .addTask(backlogExampleTask)
                    .subscribe(this::onInitialShowcaseItemsAdded, this::showError)
                    .addTo(compositeDisposable)
        }
    }

    private fun onInitialShowcaseItemsAdded() {
        ShowcaseHelper.setShowcaseItemsAdded()
    }


}