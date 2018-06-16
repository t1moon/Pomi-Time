package apps.tim.pomos.feature.ui.picker

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import javax.inject.Inject

class AddTaskFragment : DialogFragment() {

    @Inject
    lateinit var tasksViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button.setOnClickListener{
            tasksViewModel.addTask(taskTitle.text.toString())
            dismiss()
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = super.onCreateDialog(savedInstanceState)
        d.window.setBackgroundDrawableResource(R.drawable.picker_layout_background)
        return d
    }

}