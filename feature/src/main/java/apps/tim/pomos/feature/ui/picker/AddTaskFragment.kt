package apps.tim.pomos.feature.ui.picker

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.toDateLong
import apps.tim.pomos.feature.toDateString
import apps.tim.pomos.feature.ui.FRAGMENT_PAGE_KEY
import apps.tim.pomos.feature.ui.TODAY_FRAGMENT_PAGE
import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import apps.tim.pomos.feature.ui.tasks.data.Task
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*
import javax.inject.Inject


class AddTaskFragment : DialogFragment() {

    @Inject
    lateinit var tasksViewModel: TasksViewModel

    companion object {
        fun newInstance(pos: Int): AddTaskFragment {
            val fragment = AddTaskFragment()
            val bundle = Bundle()
            bundle.putInt(FRAGMENT_PAGE_KEY, pos)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button.setOnClickListener {
            val active = arguments?.get(FRAGMENT_PAGE_KEY) == TODAY_FRAGMENT_PAGE
            val task = Task(
                    id = 0,
                    title = taskTitle.text.toString(),
                    deadline = deadline.text.toString().toDateLong(),
                    isActive = active
            )
            tasksViewModel.addTask(task)
            dismiss()
        }
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        setDatePicker()
    }

    private fun setDatePicker() {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            deadline.setText(calendar.time.toDateString())
        }
        val calendar = Calendar.getInstance()
        deadline.setOnClickListener {
            val datePickerDialog = DatePickerDialog(activity, dateListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = super.onCreateDialog(savedInstanceState)
        d.window.setBackgroundDrawableResource(R.drawable.picker_layout_background)
        return d
    }

}