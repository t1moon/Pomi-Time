package apps.tim.pomos.base.ui.picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.base.PomoApp
import apps.tim.pomos.base.R
import apps.tim.pomos.base.toDateLong
import apps.tim.pomos.base.toDateString
import apps.tim.pomos.base.ui.DEFAULT_DATE_LONG
import apps.tim.pomos.base.ui.TASK_ARG
import apps.tim.pomos.base.data.Task
import kotlinx.android.synthetic.main.fragment_edit.*

class EditTaskFragment : AddTaskFragment() {
    lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
        task = arguments.get(TASK_ARG) as Task
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskTitle.setText(task.title)
        taskTitle.setSelection(taskTitle.text.length)
        active.isChecked = task.isActive

        if (task.deadline != DEFAULT_DATE_LONG) {
            deadline.setText(task.deadline.toDateString())
            removeDeadline.visibility = View.VISIBLE
        }

        delete.setOnClickListener {
            add(tasksViewModel.deleteTask(task).subscribe())
            dismiss()
        }
    }

    override fun setOkButtonClicked() {
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
            add(tasksViewModel.addTask(task).subscribe())
            dismiss()
        }
    }

}