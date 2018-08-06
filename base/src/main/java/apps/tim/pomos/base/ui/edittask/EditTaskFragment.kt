package apps.tim.pomos.base.ui.edittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.base.*
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.data.entity.Task
import apps.tim.pomos.base.ui.addtask.AddTaskFragment
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_edit.*

class EditTaskFragment : AddTaskFragment() {
    lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
        task = arguments?.get(TASK_ARG) as Task
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_edit)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInfo()
        setButtonListeners()
    }

    private fun setInfo() {
        taskTitle.setText(task.title)
        taskTitle.setSelection(taskTitle.text.length)
        active.isChecked = task.isActive
        if (task.deadline != DEFAULT_DATE_LONG) {
            deadline.setText(task.deadline.toDateString())
            removeDeadline.visibility = View.VISIBLE
        }
    }

    private fun setButtonListeners() {
        ok.setOnClickListener {
            val task = Task(
                    id = this.task.id,
                    title = taskTitle.text.toString(),
                    deadline = deadline.text.toString().toDateLong(),
                    isComplete = this.task.isComplete,
                    created = this.task.created,
                    currentPomo = this.task.currentPomo,
                    pomo = this.task.pomo,
                    isActive = active.isChecked
            )
            tasksViewModel.addTask(task)
                    .subscribe({}, this::showError)
                    .addTo(compositeDisposable)
            dismiss()
        }
        delete.setOnClickListener {
            tasksViewModel.deleteTask(task)
                    .subscribe({}, this::showError)
                    .addTo(compositeDisposable)
            dismiss()
        }
    }

}