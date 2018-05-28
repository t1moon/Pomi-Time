package apps.tim.pomos.feature.ui.tasks.pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.tasks.data.Task
import apps.tim.pomos.feature.ui.tasks.data.TasksRepository
import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tasks_list.*

class TaskListFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks_list, container, false)
    }


    private fun setTasks(items: List<Task>) {
        taskList.layoutManager = LinearLayoutManager(this@TaskListFragment.activity)
        taskList.adapter = TaskListAdapter(items, this@TaskListFragment.activity)
        taskList.adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    fun loadTasks(position: Int) {
        compositeDisposable.add(TasksViewModel(TasksRepository())
                .getTasks(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {  items: List<Task> -> this.setTasks(items) }
        )
    }
}