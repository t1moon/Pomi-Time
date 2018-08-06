package apps.tim.pomos.base.ui.edittask

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import apps.tim.pomos.base.*
import apps.tim.pomos.base.R.id.taskTitle
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.data.entity.Task
import apps.tim.pomos.base.di.ViewModelFactory
import apps.tim.pomos.base.ui.addtask.AddTaskFragment
import apps.tim.pomos.base.ui.base.BaseDialogFragment
import apps.tim.pomos.base.ui.tasks.TasksViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_edit.view.*
import java.util.*
import javax.inject.Inject

class EditTaskFragment : BaseDialogFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var tasksViewModel: TasksViewModel

    lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
        tasksViewModel = ViewModelProviders.of(activity as FragmentActivity, viewModelFactory)[TasksViewModel::class.java]
        task = arguments?.get(TASK_ARG) as Task
    }

    override fun initView(it: View) {
        setInfo(it)
        setButtonListeners(it)
    }

    private fun setInfo(view: View) {
        view.run {
            this.taskTitle.setText(task.title)
            this.taskTitle.setSelection(taskTitle.text.length)
            this.active.isChecked = task.isActive
            if (task.deadline != DEFAULT_DATE_LONG) {
                this@run.deadline.setText(task.deadline.toDateString())
                this@run.removeDeadline.visibility = View.VISIBLE
            }
        }
    }

    private fun setButtonListeners(view: View) {
        view.run {
            this.ok.setOnClickListener {
                val task = Task(
                        id = this@EditTaskFragment.task.id,
                        title = this@run.taskTitle.text.toString(),
                        deadline = this@run.deadline.text.toString().toDateLong(),
                        isComplete = this@EditTaskFragment.task.isComplete,
                        created = this@EditTaskFragment.task.created,
                        currentPomo = this@EditTaskFragment.task.currentPomo,
                        pomo = this@EditTaskFragment.task.pomo,
                        isActive = this@run.active.isChecked
                )
                tasksViewModel.addTask(task)
                        .subscribe({}, this@EditTaskFragment::showError)
                        .addTo(compositeDisposable)
                dismiss()
            }
            this.delete.setOnClickListener {
                tasksViewModel.deleteTask(task)
                        .subscribe({}, this@EditTaskFragment::showError)
                        .addTo(compositeDisposable)
                dismiss()
            }
            this.ok.setOnClickListener {
                val task = Task(
                        id = 0,
                        title = this@run.taskTitle.text.toString(),
                        deadline = this@run.deadline.text.toString().toDateLong(),
                        created = Calendar.getInstance().timeInMillis,
                        isActive = this@run.active.isChecked
                )
                tasksViewModel.addTask(task)
                        .subscribe({}, this@EditTaskFragment::showError)
                        .addTo(compositeDisposable)
                dismiss()
            }
            this.cancel.setOnClickListener {
                dismiss()
            }
            this.removeDeadline.setOnClickListener {
                this@run.deadline.setText("")
                it.visibility = View.GONE
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_edit
}