package apps.tim.pomos.base.ui.edittask

import android.os.Bundle
import android.view.View
import apps.tim.pomos.base.*
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.data.entity.Task
import apps.tim.pomos.base.ui.addtask.AddTaskFragment
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_edit.view.*

class EditTaskFragment : AddTaskFragment() {
    lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
        task = arguments?.get(TASK_ARG) as Task
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
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_edit

    override fun initView(it: View) {
        super.initView(it)
        setInfo(it)
        setButtonListeners(it)
    }
}