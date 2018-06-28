package apps.tim.pomos.feature.ui.tasks.pager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.FRAGMENT_PAGE_KEY
import apps.tim.pomos.feature.ui.TASK_ARG
import apps.tim.pomos.feature.ui.TODAY_FRAGMENT_PAGE
import apps.tim.pomos.feature.ui.base.BaseFragment
import apps.tim.pomos.feature.ui.picker.EditTaskFragment
import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import apps.tim.pomos.feature.ui.tasks.data.Task
import apps.tim.pomos.feature.ui.tasks.pager.backlog.BacklogAdapter
import apps.tim.pomos.feature.ui.tasks.pager.today.TodayTasksAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import javax.inject.Inject


class TaskListFragment : BaseFragment() {
    private var fragmentPage: Int = 0
    private lateinit var todayAdapter : TodayTasksAdapter
    private lateinit var backlogAdapter : BacklogAdapter

    @Inject
    lateinit var tasksViewModel: TasksViewModel

    companion object {
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_tasks_list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTodayAdapter()
        setBacklogAdapter()
        setTaskList()
        if (fragmentPage == TODAY_FRAGMENT_PAGE) {
            compositeDisposable.add(tasksViewModel
                    .getTodayTasks()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { items: List<Task> ->
                        taskList.adapter = todayAdapter
                        todayAdapter.submitList(items)
                    }
            )
        } else {
            compositeDisposable.add(tasksViewModel
                    .getBacklogTasks()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { items: List<Task> ->
                        taskList.adapter = backlogAdapter
                        backlogAdapter.submitList(items)
                    }
            )
        }
    }

    private fun setTaskList() {
        taskList.layoutManager = LinearLayoutManager(context)
        taskList.itemAnimator = DefaultItemAnimator()
    }

    private fun setBacklogAdapter() {
        backlogAdapter = BacklogAdapter(
                ({
                    add(tasksViewModel.activateTask(it.id).subscribe())
                }),
                ({
                    val bundle = Bundle()
                    bundle.putParcelable(TASK_ARG, it)
                    val picker = EditTaskFragment()
                    picker.arguments = bundle
                    picker.show((this@TaskListFragment.activity as AppCompatActivity).fragmentManager, "Picker")
                    true
                }))
    }

    private fun setTodayAdapter() {
        todayAdapter = TodayTasksAdapter(
                ({
                    val bundle = Bundle()
                    bundle.putParcelable(TASK_ARG, it)
                    this@TaskListFragment.findNavController().navigate(R.id.action_tasksFragment_to_timerFragment, bundle)
                }),
                ({
                    val bundle = Bundle()
                    bundle.putParcelable(TASK_ARG, it)
                    val picker = EditTaskFragment()
                    picker.arguments = bundle
                    picker.show((this@TaskListFragment.activity as AppCompatActivity).fragmentManager, "Picker")
                    true
                }),
                ({ task: Task, checked: Boolean ->
                    add(tasksViewModel.markIsCompleteTaskById(checked, task.id).subscribe())
                }))
    }

}