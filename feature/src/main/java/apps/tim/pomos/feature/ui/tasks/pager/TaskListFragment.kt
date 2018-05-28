package apps.tim.pomos.feature.ui.tasks.pager

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.base.BaseFragment
import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import apps.tim.pomos.feature.ui.tasks.data.Task
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tasks_list.*


class TaskListFragment : BaseFragment() {


    companion object {
        fun newInstance(pos: Int) : TaskListFragment {
            val fragment = TaskListFragment()
            val bundle = Bundle()
            bundle.putInt("pos", pos)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable.add(TasksViewModel(TasksRepository())
                .getTasks(this@TaskListFragment.arguments?.get("pos") as Int)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {  items: List<Task> -> this.setTasks(items) }
        )
    }
    private fun setTasks(items: List<Task>) {
        taskList.layoutManager = LinearLayoutManager(this@TaskListFragment.activity)
//        val itemDecor = DividerItemDecoration(this@TaskListFragment.activity,
//                (taskList.layoutManager as LinearLayoutManager).orientation)
//        taskList.addItemDecoration(itemDecor)

        taskList.adapter = TaskListAdapter(items, this@TaskListFragment.activity)
        taskList.adapter.notifyDataSetChanged()
    }

    fun loadTasks(position: Int) {
//        compositeDisposable.add(TasksViewModel(TasksRepository())
//                .getTasks(position)!!
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {  items: List<Task> -> this.setTasks(items) }
//        )
    }
}