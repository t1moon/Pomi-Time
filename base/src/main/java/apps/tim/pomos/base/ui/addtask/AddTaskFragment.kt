package apps.tim.pomos.base.ui.addtask

import android.app.DatePickerDialog
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import apps.tim.pomos.base.R
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.data.entity.Task
import apps.tim.pomos.base.di.ViewModelFactory
import apps.tim.pomos.base.toDateLong
import apps.tim.pomos.base.toDateString
import apps.tim.pomos.base.ui.base.BaseDialogFragment
import apps.tim.pomos.base.ui.tasks.TasksViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*
import javax.inject.Inject


open class AddTaskFragment : BaseDialogFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    protected lateinit var tasksViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
        tasksViewModel = ViewModelProviders.of(activity as FragmentActivity, viewModelFactory)[TasksViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_add)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestKeyboardForInput()
        setButtonListeners()
        setDatePicker()
    }

    private fun setButtonListeners() {
        ok.setOnClickListener {
            val task = Task(
                    id = 0,
                    title = taskTitle.text.toString(),
                    deadline = deadline.text.toString().toDateLong(),
                    created = Calendar.getInstance().timeInMillis,
                    isActive = active.isChecked
            )
            tasksViewModel.addTask(task)
                    .subscribe({},this::showError)
                    .addTo(compositeDisposable)
            dismiss()
        }
        cancel.setOnClickListener {
            dismiss()
        }
        removeDeadline.setOnClickListener {
            deadline.setText("")
            it.visibility = View.GONE
        }
    }

    private fun requestKeyboardForInput() {
        taskTitle.requestFocus()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    private fun setDatePicker() {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            deadline.setText(calendar.time.toDateString())
            removeDeadline.visibility = View.VISIBLE
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

    // Set round corners
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = super.onCreateDialog(savedInstanceState)
        d.window.setBackgroundDrawableResource(R.drawable.picker_layout_background)
        return d
    }
}