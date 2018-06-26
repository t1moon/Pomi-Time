package apps.tim.pomos.feature.ui.picker

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.TASK_ARG
import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import apps.tim.pomos.feature.ui.tasks.data.Task
import kotlinx.android.synthetic.main.fragment_edit.*
import javax.inject.Inject

class EditTaskFragment : DialogFragment() {
    lateinit var task: Task

    @Inject
    lateinit var tasksViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
        task = arguments.get(TASK_ARG) as Task
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        taskTitle.setText(task.title)
        taskTitle.setSelection(taskTitle.text.toString().length)
        ok.setOnClickListener{
            tasksViewModel.updateTitleById(taskTitle.text.toString(), task.id)
            dismiss()
        }
        delete.setOnClickListener {
            tasksViewModel.deleteTask(task)
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = super.onCreateDialog(savedInstanceState)
        d.window.setBackgroundDrawableResource(R.drawable.picker_layout_background)
        return d
    }
}