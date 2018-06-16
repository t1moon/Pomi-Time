package apps.tim.pomos.feature.ui.tasks.pager

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.R.id.taskList
import apps.tim.pomos.feature.ui.base.BaseFragment
import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import apps.tim.pomos.feature.ui.tasks.data.Task
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import javax.inject.Inject


class TaskListFragment : BaseFragment() {

    @Inject
    lateinit var tasksViewModel: TasksViewModel


    companion object {
        private const val position: String = "POSITION"

        fun newInstance(pos: Int): TaskListFragment {
            val fragment = TaskListFragment()
            val bundle = Bundle()
            bundle.putInt(position, pos)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_tasks_list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable.add(tasksViewModel
                .getTasks(this@TaskListFragment.arguments?.get(position) as Int)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items: List<Task> -> this.setTasks(items) }
        )
    }

    private fun setTasks(items: List<Task>) {
        context?.let {
            taskList.layoutManager = LinearLayoutManager(context)
            val decoration = ListSeparator(context as Context, PomoApp.color(R.color.listSeparator), 1f)
            taskList.addItemDecoration(decoration)
            taskList.adapter = TaskListAdapter(items, context as Context)
            taskList.adapter.notifyDataSetChanged()
        }
    }
}