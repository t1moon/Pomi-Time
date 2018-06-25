package apps.tim.pomos.feature.ui.tasks.pager

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.FRAGMENT_PAGE_KEY
import apps.tim.pomos.feature.ui.TODAY_FRAGMENT_PAGE
import apps.tim.pomos.feature.ui.base.BaseFragment
import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import apps.tim.pomos.feature.ui.tasks.data.Task
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import javax.inject.Inject


class TaskListFragment : BaseFragment() {
    private var fragmentPage: Int = 0

    @Inject
    lateinit var tasksViewModel: TasksViewModel

    companion object {
        fun  newInstance(pos: Int): TaskListFragment {
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
        if (fragmentPage == TODAY_FRAGMENT_PAGE) {
            compositeDisposable.add(tasksViewModel
                    .getTodayTasks()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { items: List<Task> -> this.setTasks(items) }
            )
        } else {
            compositeDisposable.add(tasksViewModel
                    .getBacklogTasks()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { items: List<Task> -> this.setBacklog(items) }
            )
        }

    }

    private fun setTasks(items: List<Task>) {
        context?.let {
            taskList.layoutManager = LinearLayoutManager(context)
            val adapter = TodayTasksAdapter(items, context as Context)
            taskList.adapter = adapter
            (taskList.adapter as TodayTasksAdapter).notifyDataSetChanged()
        }
    }

    private fun setBacklog(items: List<Task>) {
        context?.let {
            taskList.layoutManager = LinearLayoutManager(context)
            val adapter = BacklogAdapter(items, context as Context)
            setBacklogAdapterClickListeners(adapter)
            taskList.adapter = adapter
            (taskList.adapter as BacklogAdapter).notifyDataSetChanged()
        }
    }


    private fun setBacklogAdapterClickListeners(adapter: BacklogAdapter) {
        add(adapter.activateTaskClickEvent
                .subscribe {
                    tasksViewModel.activateTask(it.id)
                }
        )
    }

}